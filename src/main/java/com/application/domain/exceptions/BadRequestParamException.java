package com.application.domain.exceptions;

public class BadRequestParamException extends BusTicketException {
    public BadRequestParamException() {
    }

    public BadRequestParamException(String message) {
        super(message);
    }
}
