package org.ies.tierno.model;

public record Book(Long id, String isbn, String title, String author, int publicationYear, int pages) {
}