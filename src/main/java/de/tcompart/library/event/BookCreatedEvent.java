package de.tcompart.library.event;

import java.time.Instant;
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

  public List<String> getAuthors() {
    return authors;
  }

  public String getTitle() {
    return title;
  }

  public Instant getCreated() {
    return created;
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
