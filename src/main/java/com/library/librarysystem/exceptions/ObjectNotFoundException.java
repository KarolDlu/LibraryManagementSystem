package com.library.librarysystem.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String objectName, Long objectId) {
        super(objectName + " with given id: " + objectId + " does not exists.");
    }

    public ObjectNotFoundException(String objectName, String field, String fieldValue) {
        super(objectName + " with given " + field + ": " + fieldValue + " does not exists.");
    }
}
