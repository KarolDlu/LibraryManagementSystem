package com.library.librarysystem.exceptions;

public class ReservationNotFoundException extends ObjectNotFoundException {

    public ReservationNotFoundException(Long memberId, Long bookId) {
        super("Reservation with memberId: (" + memberId + ") and bookId: (" + bookId + ") does not exists.");
    }
}
