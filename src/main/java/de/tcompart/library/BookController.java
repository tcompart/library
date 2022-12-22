package de.tcompart.library;

import de.tcompart.library.web.Book;
import java.util.Collection;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

  private final List<Book> books;

  public BookController(List<Book> books) {
    this.books = books;
  }

  @GetMapping("/books")
  Collection<Book> books() {
    return books;
  }

  Book byId() {
    return new Book();
  }
}
