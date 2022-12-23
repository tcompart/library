package de.tcompart.library.event;

import java.time.Instant;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Event implements Comparable<Event> {

  protected Instant created;

  protected String name = this.getClass().getSimpleName();

  public UUID getId() {
    return UUID.randomUUID();
  }

  public Instant getCreated() {
    return created;
  }

  void accept(EventVisitor<? super Event> eventVisitor) {
    eventVisitor.visit(this);
  }

  @Override
  public int compareTo(Event o) {
    return created.compareTo(o.created);
  }
}
