package org.ies.tierno.service;

import org.ies.tierno.controller.dto.BookRequest;
import org.ies.tierno.model.Book;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public BookService() {
        create(new BookRequest("9788420412146", "Don Quijote de la Mancha", "Miguel de Cervantes", 1605, 1376));
        create(new BookRequest("9788437604596", "La Regenta", "Leopoldo Alas Clarín", 1884, 1040));
        create(new BookRequest("9788420665245", "Novelas ejemplares", "Miguel de Cervantes", 1613, 640));
    }

    /**
     * Devuelve los libros cuyo autor y título contienen los textos indicados,
     * sin distinguir mayúsculas. Un filtro null no se aplica.
     */
    public List<Book> search(String author, String title) {
        return books.values().stream()
                .filter(book -> author == null || containsIgnoreCase(book.author(), author))
                .filter(book -> title == null || containsIgnoreCase(book.title(), title))
                .sorted(Comparator.comparing(Book::id))
                .toList();
    }

    public Optional<Book> findById(long id) {
        return Optional.ofNullable(books.get(id));
    }

    /**
     * @throws DuplicateIsbnException si ya existe un libro con ese ISBN
     */
    public synchronized Book create(BookRequest request) {
        if (isbnInUse(request.isbn(), null)) {
            throw new DuplicateIsbnException(request.isbn());
        }
        long id = nextId.getAndIncrement();
        Book book = toBook(id, request);
        books.put(id, book);
        return book;
    }

    /**
     * Devuelve Optional.empty() si el libro no existe.
     *
     * @throws DuplicateIsbnException si el ISBN pertenece a otro libro
     */
    public synchronized Optional<Book> update(long id, BookRequest request) {
        if (!books.containsKey(id)) {
            return Optional.empty();
        }
        if (isbnInUse(request.isbn(), id)) {
            throw new DuplicateIsbnException(request.isbn());
        }
        Book book = toBook(id, request);
        books.put(id, book);
        return Optional.of(book);
    }

    /**
     * Devuelve true si el libro existía y se ha eliminado.
     */
    public synchronized boolean delete(long id) {
        return books.remove(id) != null;
    }

    private boolean isbnInUse(String isbn, Long excludedId) {
        return books.values().stream()
                .anyMatch(book -> book.isbn().equals(isbn) && !book.id().equals(excludedId));
    }

    private static Book toBook(long id, BookRequest request) {
        return new Book(id, request.isbn(), request.title(), request.author(),
                request.publicationYear(), request.pages());
    }

    private static boolean containsIgnoreCase(String text, String fragment) {
        return text.toLowerCase().contains(fragment.toLowerCase());
    }
}