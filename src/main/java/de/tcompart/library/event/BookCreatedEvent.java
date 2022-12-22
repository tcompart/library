package de.tcompart.library.event;

import de.tcompart.library.web.CreateBookRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

public class BookCreatedEvent extends Event {
  private final List<String> authors;
  private final String title;
  private final Instant published;

  public BookCreatedEvent(List<String> authors, String title, Instant created, Instant published) {
    this.authors = authors;
    this.title = title;
    this.created = created;
    this.published = published;
  }

  public static BookCreatedEvent from(CreateBookRequest createBookRequest) {
    return new BookCreatedEvent(createBookRequest.getAuthors(), createBookRequest.getTitle(), Instant.now(),
        toInstant(createBookRequest));
  }

  private static Instant toInstant(CreateBookRequest createBookRequest) {
    return createBookRequest.getPublished().atStartOfDay(ZoneId.systemDefault()).toInstant();
  }

  public List<String> getAuthors() {
    return authors;
  }

  public String getTitle() {
    return title;
  }

  public Instant getPublished() {
    return published;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    BookCreatedEvent that = (BookCreatedEvent) o;
    return Objects.equals(authors, that.authors) &&
        Objects.equals(title, that.title) &&
        Objects.equals(published, that.published);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), authors, title, published);
  }
}
