package de.tcompart.library.event;

public interface EventVisitor<T extends Event> {

  void visit(T event);

}
