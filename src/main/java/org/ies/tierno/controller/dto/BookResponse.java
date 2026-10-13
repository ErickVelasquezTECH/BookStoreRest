package org.ies.tierno.controller.dto;

import org.ies.tierno.model.Book;

public record BookResponse(
        String isbn,
        String title,
        String author,
        int publicationYear,
        int pages
) {

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.isbn(), book.title(),
                book.author(), book.publicationYear(), book.pages());
    }
}
