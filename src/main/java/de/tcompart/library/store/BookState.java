package de.tcompart.library.store;

import de.tcompart.library.event.Event;
import de.tcompart.library.web.Book;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BookState {

  private final Set<Event> eventsVisited = new HashSet<>();
  private final Set<Book> booksExisting = new HashSet<>();

  public void addEvent(Event event) {
    eventsVisited.add(event);
  }

  public void addBook(Book book) {
    booksExisting.add(book);
  }

  public Collection<Book> getBooks() {
    return booksExisting;
  }
}
