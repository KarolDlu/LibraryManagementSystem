package com.library.librarysystem.exceptions;

public class MaxNumberOfBorrowedBooksReachedException extends RuntimeException {

    public MaxNumberOfBorrowedBooksReachedException(Long memberId, int maxNumberOfBooks) {
        super("Member with id: " + memberId + " has borrowed the maximum number of books (" + maxNumberOfBooks + ").");
    }
}
