package com.library.librarysystem.exceptions;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(Long bookId) {
        super("A copy of book with id: " + bookId + " is unavailable.");
    }
}
