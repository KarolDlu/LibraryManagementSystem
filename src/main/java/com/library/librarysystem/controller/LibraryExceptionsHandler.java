package com.library.librarysystem.controller;

import com.library.librarysystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LibraryExceptionsHandler {

    @ExceptionHandler(value = ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundExceptions(ObjectNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ObjectAlreadyExistsException.class)
    public ResponseEntity<String> handleObjectAlreadyExists(ObjectAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BookNotAvailableException.class)
    public ResponseEntity<String> handleBookNotAvailable(BookNotAvailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MemberAccountBlockedException.class)
    public ResponseEntity<String> handleMemberAccountBlocked(MemberAccountBlockedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MaxNumberOfReservationReachedException.class)
    public ResponseEntity<String> MaxNumberOfReservationReached(MaxNumberOfReservationReachedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MaxNumberOfBorrowedBooksReachedException.class)
    public ResponseEntity<String> MaxNumberOfBorrowedBooksReached(MaxNumberOfBorrowedBooksReachedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
