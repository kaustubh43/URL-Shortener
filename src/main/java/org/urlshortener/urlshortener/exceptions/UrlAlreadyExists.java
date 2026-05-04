package org.urlshortener.urlshortener.exceptions;

public class UrlAlreadyExists extends RuntimeException {
    public UrlAlreadyExists(String message) {
        super(message);
    }
}
