package de.tcompart.library.store;


import de.tcompart.library.event.Event;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Stream;


public interface ReadEventStore {

  default Collection<? extends Event> getAll() {
    return Collections.emptyList();
  }

  default Stream<? extends Event> filterBy(Predicate<? extends Event>... eventFilter) {
    return Stream.of();
  }

}
