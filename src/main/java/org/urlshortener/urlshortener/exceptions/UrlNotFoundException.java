package org.urlshortener.urlshortener.exceptions;
/*
    User Exception, when long URL is not found for the supplied short URL.
 */
public class UrlNotFoundException extends Exception {
    public UrlNotFoundException(String message) {
        super(message);
    }
}
