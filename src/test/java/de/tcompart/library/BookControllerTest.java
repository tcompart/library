package de.tcompart.library;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.tcompart.library.event.BookCreatedEvent;
import de.tcompart.library.store.InMemoryStore;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BookControllerTest {

  @Test
  void getEmptyListOfBooks() throws Exception {
    BookController bookController =
        new BookController(new InMemoryStore(emptyList()), new InMemoryStore(emptyList()));
    when(bookController)
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  void getOneBookInList() throws Exception {
    BookController bookController =
        new BookController(
            new InMemoryStore(singletonList(createEmptyBook())), new InMemoryStore(emptyList()));
    when(bookController)
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(content().json("[{}]"));
  }

  @Test
  void getOneBookViaId() throws Exception {
    BookController bookController =
        new BookController(
            new InMemoryStore(List.of(createFamousBook())), new InMemoryStore(emptyList()));
    when(bookController).perform(get("/book/123")).andExpect(status().isNotFound());
  }

  @Test
  void findByAuthor() throws Exception {
    BookController bookController =
        new BookController(
            new InMemoryStore(List.of(createFamousBook())), new InMemoryStore(emptyList()));
    when(bookController)
        .perform(get("/books?byAuthor=Sally%20Rooney"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getOneBookThatExists() throws Exception {
    BookController bookController =
        new BookController(
            new InMemoryStore(List.of(createFamousBook())), new InMemoryStore(emptyList()));
    when(bookController)
        .perform(get("/books?byAuthor=Sally%20Rooney"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().json("[{}]"))
        .andExpect(jsonPath("$[0].authors[0].name").value("Sally Rooney"))
        .andExpect(jsonPath("$[0].created").value("2018-08-28"))
        .andExpect(jsonPath("$[0].published").value("2018-02-01"))
        .andExpect(jsonPath("$[0].title").value("Normal People"))
        .andDo(print());
  }

  private BookCreatedEvent createEmptyBook() {
    return new BookCreatedEvent(emptyList(), "", Instant.now(), Instant.now());
  }

  private MockMvc when(BookController bookController) {
    return MockMvcBuilders.standaloneSetup(bookController).build();
  }

  private BookCreatedEvent createFamousBook() {
    Instant publishingDate =
        LocalDate.of(2018, 8, 28).atStartOfDay(ZoneId.systemDefault()).toInstant();
    Instant creationDate =
        LocalDate.of(2018, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
    return new BookCreatedEvent(
        List.of("Sally Rooney"), "Normal People", publishingDate, creationDate);
  }
}
