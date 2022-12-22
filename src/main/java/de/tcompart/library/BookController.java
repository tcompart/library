package de.tcompart.library;

import de.tcompart.library.event.BookCreatedEvent;
import de.tcompart.library.event.BookStockVisitor;
import de.tcompart.library.store.BookState;
import de.tcompart.library.store.ReadEventStore;
import de.tcompart.library.store.WriteEventStore;
import de.tcompart.library.web.Author;
import de.tcompart.library.web.Book;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

  private final ReadEventStore eventStore;
  private final WriteEventStore writeEventStore;

  public BookController(ReadEventStore eventStore, WriteEventStore writeEventStore) {
    this.eventStore = eventStore;
    this.writeEventStore = writeEventStore;
  }

  @GetMapping(value = "/books", produces = "application/json;charset=UTF-8")
  ResponseEntity<Collection<Book>> books(
      @RequestParam(value = "byAuthor", required = false) String... authors) {
    Collection<Book> value = toCollectionOfBooks();
    if (authors != null) {
      List<String> namesOfAuthors =
          Stream.of(authors).map(name -> URLDecoder.decode(name, StandardCharsets.UTF_8))
              .toList();
      value = value.stream().filter(b -> b.authors().stream().map(Author::name).toList().containsAll(namesOfAuthors)).toList();
    }
    return ResponseEntity.of(Optional.of(value));
  }

  private Collection<Book> toCollectionOfBooks() {
    BookState bookState = new BookState();
    BookStockVisitor bookStockVisitor = new BookStockVisitor(bookState);
    eventStore.getAll().stream()
        .filter(BookCreatedEvent.class::isInstance)
        .forEach(event -> bookStockVisitor.visit((BookCreatedEvent) event));
    return bookState.getBooks();
  }

  @GetMapping("/book/{id}")
  ResponseEntity<Book> byId(String id) {
    return ResponseEntity.notFound().build();
  }
}
