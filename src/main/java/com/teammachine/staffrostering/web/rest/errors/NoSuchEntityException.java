package com.teammachine.staffrostering.web.rest.errors;

public class NoSuchEntityException extends CustomParameterizedException {

    public NoSuchEntityException(String message, Object id) {
        super(message, String.valueOf(id));
    }
}
