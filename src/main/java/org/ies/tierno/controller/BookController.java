package org.ies.tierno.controller;

import org.ies.tierno.controller.dto.BookResponse;
import org.ies.tierno.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(service.search(author, title)
                .stream()
                .map(BookResponse::from)
                .toList()
        );

    }
}
