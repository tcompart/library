package de.tcompart.library.store;

import de.tcompart.library.event.Event;

public interface WriteEventStore {

  void addEvent(Event event);

}
