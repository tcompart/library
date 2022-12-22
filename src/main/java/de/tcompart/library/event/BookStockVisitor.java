package de.tcompart.library.event;

import de.tcompart.library.store.BookState;
import de.tcompart.library.web.Author;
import de.tcompart.library.web.Book;
import java.util.List;

public record BookStockVisitor(BookState state) implements EventVisitor<BookCreatedEvent> {

  @Override
  public void visit(BookCreatedEvent event) {
    List<Author> authors = event.getAuthors().stream().map(Author::new).toList();
    state.addBook(new Book(authors, event.getTitle(), event.getCreated(), event.getPublished()));
    state.addEvent(event);
  }
}
