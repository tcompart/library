package de.tcompart.library.event;

import java.time.Instant;
import java.util.List;

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
}
