package com.library.librarysystem.exceptions;

public class BookAlreadyExists extends ObjectAlreadyExistsException {

    public BookAlreadyExists(String isbn) {
        super("Book with given isbn: " + isbn +" already exists.");
    }
}
