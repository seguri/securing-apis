package com.application.domain.exceptions;

public class UnexpectedFileTypeException extends PhotoUploadException {

    @Override
    public String getMessage() {
        return "unsupported upload format file";
    }
}
