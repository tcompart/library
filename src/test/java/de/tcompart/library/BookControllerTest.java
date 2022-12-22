package de.tcompart.library;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.tcompart.library.web.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BookControllerTest {

  @Test
  void getEmptyListOfBooks() throws Exception {
    BookController bookController = new BookController(Collections.emptyList());
    when(bookController)
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  private MockMvc when(BookController bookController) {
    return MockMvcBuilders.standaloneSetup(bookController)
        .build();
  }

  @Test
  void getOneBookInList() throws Exception {
    BookController bookController = new BookController(List.of(new Book()));
    when(bookController).perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(content().json("[{}]"));
  }

}