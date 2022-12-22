package de.tcompart.library.store;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import de.tcompart.library.event.Event;
import de.tcompart.library.event.SimpleEvent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Iterables;

class InMemoryStoreTest {

  @Test
  void eventStoreShouldBeSorted() {
    SimpleEvent oldest = new SimpleEvent("old");
    SimpleEvent shouldAtTop = new SimpleEvent("shouldAtTop");
    List<Event> events = List.of(oldest, shouldAtTop, new SimpleEvent(oldest));
    InMemoryStore inMemoryStore = new InMemoryStore(events);
    assertThat(inMemoryStore.getAll()).isEqualTo(List.of(shouldAtTop, oldest, oldest));
  }
}
