package com.library.librarysystem.exceptions;

public class BookAlreadyReservedException extends ObjectAlreadyExistsException {

    public BookAlreadyReservedException(Long bookId) {
        super("Renewal impossible. A copy of book with: " + bookId + " has waiting reservation.");
    }
}
