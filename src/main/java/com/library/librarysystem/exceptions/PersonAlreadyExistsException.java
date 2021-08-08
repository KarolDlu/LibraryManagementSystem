package com.library.librarysystem.exceptions;

public class PersonAlreadyExistsException extends ObjectAlreadyExistsException {

    public PersonAlreadyExistsException(String email) {
        super("Person with given email: " + email + " already exists.");
    }

}
