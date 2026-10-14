package org.ies.tierno.controller;

import jakarta.validation.Valid;
import org.ies.tierno.controller.dto.BookRequest;
import org.ies.tierno.controller.dto.BookResponse;
import org.ies.tierno.model.Book;
import org.ies.tierno.service.BookService;
import org.ies.tierno.service.DuplicateIsbnException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(
                service.search(author, title)
                        .stream()
                        .map(BookResponse::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable("id") long id) {
        return service.findById(id)
                .map(book -> ResponseEntity.ok(BookResponse.from(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        try {
            Book book = service.create(request);

            URI location = URI.create("api/books/" + book.id());

            return ResponseEntity.created(location)
                    .body(BookResponse.from(book));

        } catch (DuplicateIsbnException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> putBook(
            @PathVariable("id") long id,
            @Valid @RequestBody BookRequest request
    ) {
        try {
            return service.update(id, request)
                    .map(book -> ResponseEntity.ok(BookResponse.from(book)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DuplicateIsbnException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
