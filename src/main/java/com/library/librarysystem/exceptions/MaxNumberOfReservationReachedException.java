package com.library.librarysystem.exceptions;

public class MaxNumberOfReservationReachedException extends RuntimeException {
    public MaxNumberOfReservationReachedException(int maxNumberOfReservation) {
        super("Member reached max number of reservations(" + maxNumberOfReservation + ")");
    }
}
