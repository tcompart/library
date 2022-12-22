package de.tcompart.library.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Event {

  protected Instant created;

  protected String name = this.getClass().getSimpleName();

  public UUID getId() {
    return UUID.randomUUID();
  }

  void accept(EventVisitor eventVisitor) {
    eventVisitor.visit(this);
  }

  @Override
  public String toString() {
    return "Event{" +
        "name=" + name +
        ", created=" + created +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(created, event.created) && Objects.equals(name, event.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(created, name);
  }
}
