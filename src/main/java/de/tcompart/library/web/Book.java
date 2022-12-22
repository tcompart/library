package de.tcompart.library.web;

import java.util.List;

public record Book(List<Author> authors, String title, String created, String published) {
}
