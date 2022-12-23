package de.tcompart.library.event;

import de.tcompart.library.web.CreateBookRequest;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class BookCreatedEvent extends Event {
  private final List<String> authors;
  private final String title;
  private final Instant published;

  public static BookCreatedEvent from(CreateBookRequest createBookRequest) {
    return new BookCreatedEvent(createBookRequest.getAuthors(), createBookRequest.getTitle(), toInstant(createBookRequest));
  }

  private static Instant toInstant(CreateBookRequest createBookRequest) {
    return createBookRequest.getPublished().atStartOfDay(ZoneId.systemDefault()).toInstant();
  }

}
