package de.tcompart.library.event;

import de.tcompart.library.store.BookState;
import de.tcompart.library.web.Author;
import de.tcompart.library.web.Book;
import java.util.List;

public class BookStockVisitor implements EventVisitor<BookCreatedEvent> {

  private final BookState state;

  public BookStockVisitor(BookState state) {
    this.state = state;
  }

  @Override
  public void visit(BookCreatedEvent event) {
    List<Author> authors = event.getAuthors().stream().map(Author::new).toList();
    state.addBook(new Book(authors, event.getTitle(), event.getCreated(), event.getPublished()));
    state.addEvent(event);
  }
}
