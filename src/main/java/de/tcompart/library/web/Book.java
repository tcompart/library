package de.tcompart.library.web;

import java.time.Instant;
import java.util.List;

public record Book(List<Author> authors, String title, Instant created, Instant published) {
}
