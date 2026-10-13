package org.ies.tierno.service;

public class DuplicateIsbnException extends RuntimeException {

    public DuplicateIsbnException(String isbn) {
        super("El libron con isbn: " + isbn + " ya existe");
    }
}