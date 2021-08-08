package com.library.librarysystem.exceptions;

public class ReservationAlreadyExistsException extends ObjectAlreadyExistsException {

    public ReservationAlreadyExistsException(Long memberId, Long bookId) {
        super("Member with id: " + memberId + ", already has reserved book with id: " + bookId);
    }
}
