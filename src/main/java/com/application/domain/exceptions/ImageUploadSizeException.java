package com.application.domain.exceptions;

public class ImageUploadSizeException extends PhotoUploadException {
    private String msg;

    public ImageUploadSizeException(int length) {
        msg = String.format("upload file (%d bytes) size exceeded maximum limit", length);
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
