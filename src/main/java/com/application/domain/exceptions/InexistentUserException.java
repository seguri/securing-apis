package com.application.domain.exceptions;

public class InexistentUserException extends BusTicketException {
    private final Long id;

    public InexistentUserException(Long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("user with id %s does not exist", id.toString());
    }
}
