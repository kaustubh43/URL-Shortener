package org.urlshortener.urlshortener.exceptions;

public class UrlExpired extends RuntimeException {
    public UrlExpired(String message) {
        super(message);
    }
}
