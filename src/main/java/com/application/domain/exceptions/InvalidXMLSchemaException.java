package com.application.domain.exceptions;

import org.xml.sax.SAXException;

public class InvalidXMLSchemaException extends BusTicketException {

    public InvalidXMLSchemaException(SAXException e) {
        super(e);
    }
}
