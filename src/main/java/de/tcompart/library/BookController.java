package de.tcompart.library;

import de.tcompart.library.event.BookCreatedEvent;
import de.tcompart.library.event.BookStockVisitor;
import de.tcompart.library.store.BookState;
import de.tcompart.library.store.ReadEventStore;
import de.tcompart.library.store.WriteEventStore;
import de.tcompart.library.web.Author;
import de.tcompart.library.web.Book;
import de.tcompart.library.web.CreateBookRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/books"})
public class BookController {

  private final ReadEventStore eventStore;
  private final WriteEventStore writeEventStore;

  public BookController(ReadEventStore eventStore, WriteEventStore writeEventStore) {
    this.eventStore = eventStore;
    this.writeEventStore = writeEventStore;
  }

  @PostMapping
  ResponseEntity<Book> addBook(@RequestBody @Validated CreateBookRequest createBookRequest) {
    BookCreatedEvent bookCreatedEvent = BookCreatedEvent.from(createBookRequest);
    writeEventStore.addEvent(bookCreatedEvent);
    return ResponseEntity.ok().build();
  }

  @GetMapping(produces = "application/json;charset=UTF-8")
  ResponseEntity<Collection<Book>> books(
      @RequestParam(value = "byAuthor", required = false) String... authors) {
    Collection<Book> value = toCollectionOfBooks();
    if (authors != null) {
      List<String> namesOfAuthors =
          Stream.of(authors).map(name -> URLDecoder.decode(name, StandardCharsets.UTF_8)).toList();
      value =
          value.stream()
              .filter(
                  b -> b.authors().stream().map(Author::name).toList().containsAll(namesOfAuthors))
              .toList();
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

  @GetMapping("/{id}")
  ResponseEntity<Book> byId(@PathVariable("id") String id) {
    BookState bookState = new BookState();
    BookStockVisitor bookStockVisitor = new BookStockVisitor(bookState);
    UUID uuid = UUID.fromString(id);
    eventStore.getAll().stream()
        .filter(e -> e.getId().equals(uuid))
        .filter(BookCreatedEvent.class::isInstance)
        .forEach(e -> bookStockVisitor.visit((BookCreatedEvent) e));
    Iterator<Book> iterator = bookState.getBooks().iterator();
    if (iterator.hasNext()) {
      ResponseEntity.ok(iterator.next());
    }
    return ResponseEntity.notFound().build();
  }
}
