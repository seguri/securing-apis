package com.application.domain.exceptions;

public class BusTicketException extends RuntimeException {
    public BusTicketException() {
    }

    public BusTicketException(Throwable cause) {
        super(cause);
    }

    public BusTicketException(String message) {
        super(message);
    }
}
