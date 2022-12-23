package de.tcompart.library;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.tcompart.library.event.BookCreatedEvent;
import de.tcompart.library.store.InMemoryStore;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
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
    when(bookController).perform(get("/books?byAuthor=Sally%20Rooney")).andExpect(status().isOk());
  }

  @Test
  void addNewBookNeedsToBeValid() throws Exception {
    InMemoryStore writeEventStore = new InMemoryStore();
    BookController bookController = new BookController(new InMemoryStore(), writeEventStore);
    when(bookController)
        .perform(post("/books").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.errors").exists())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors", hasSize(4)))
        .andExpect(jsonPath("$.errors", hasItem("Invalid list of authors. Specify at least one.")))
        .andExpect(jsonPath("$.errors", hasItem("No authors specified. Specify at least one.")))
        .andExpect(jsonPath("$.errors", hasItem("Invalid publication date. It is not allowed to be null. Please specify in the pattern of dd-MM-yyyy.")))
        .andExpect(jsonPath("$.errors", hasItem("Invalid title. It is not allowed to be empty.")))
        .andDo(print());
  }

  @Test
  void addNewBookButInvalidPublicationDate() throws Exception {
    String bookRequest = """
        {
        "authors": ["Lena Magdalena"],
        "title": "hallo",
        "published": "20-324-2022"
        }
        """;

    InMemoryStore writeEventStore = new InMemoryStore();
    BookController bookController = new BookController(new InMemoryStore(), writeEventStore);
    when(bookController)
        .perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(bookRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0]").value("Invalid date specified."));
  }

  @Test
  void addNewBook() throws Exception {
    String bookRequest = """
        {
        "authors": ["Anna-Maria"],
        "title": "moin",
        "published": "20-02-2022"
        }
        """;

    InMemoryStore writeEventStore = new InMemoryStore();
    BookController bookController = new BookController(new InMemoryStore(), writeEventStore);
    when(bookController)
        .perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(bookRequest))
        .andExpect(status().isOk());
    assertThat(writeEventStore.getAll()).hasSize(1);
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
        .andExpect(jsonPath("$[0].published").value("2018-08-28"))
        .andExpect(jsonPath("$[0].title").value("Normal People"))
        .andDo(print());
  }

  private BookCreatedEvent createEmptyBook() {
    return new BookCreatedEvent(emptyList(), "", Instant.now());
  }

  private MockMvc when(BookController bookController) {
    return MockMvcBuilders.standaloneSetup(bookController)
        .setControllerAdvice(new ValidationControllerAdvice()).build();
  }

  private BookCreatedEvent createFamousBook() {
    Instant publishingDate =
        LocalDate.of(2018, 8, 28).atStartOfDay(ZoneId.systemDefault()).toInstant();
    return new BookCreatedEvent(
        List.of("Sally Rooney"), "Normal People", publishingDate);
  }
}
