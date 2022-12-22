package de.tcompart.library.store;

import de.tcompart.library.event.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class InMemoryStore implements ReadEventStore, WriteEventStore {

  private final List<Event> events;

  public InMemoryStore() {
    this(new ArrayList<>());
  }

  public InMemoryStore(List<Event> events) {
    this.events = events;
  }

  public Collection<? extends Event> getAll() {
    return events;
  }

  public Stream<? extends Event> filterBy(Predicate<? extends Event>... eventFilter) {
    return events.stream();
  }

  @Override
  public void addEvent(Event event) {
    events.add(event);
  }
}
