package com.application.domain.utils;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> elements;
    private String cursor;

    public PaginatedResponse(List<T> elements, String cursor) {
        this.elements = elements;
        this.cursor = cursor;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}