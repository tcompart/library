package de.tcompart.library.event;

import java.time.Instant;

public class SimpleEvent extends Event {

  public SimpleEvent(String name) {
    this.name = name;
    this.created = Instant.now();
  }

  public SimpleEvent(SimpleEvent clone) {
    this.name = clone.name;
    this.created = clone.created;
  }

}
