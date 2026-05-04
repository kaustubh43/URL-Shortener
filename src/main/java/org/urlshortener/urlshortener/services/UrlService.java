package org.urlshortener.urlshortener.services;

import java.time.Instant;

public interface UrlService {
    /**
     * Shortens a URL with no expiration date.
     */
    String shortenUrl(String originalUrl);

    /**
     * Shortens a URL that will expire at a specific point in time.
     */
    String shortenUrl(String originalUrl, Instant expiresAt);

    /**
     * Resolves a short alias back to the original URL.
     * @throws org.urlshortener.urlshortener.exceptions.UrlNotFoundException if the alias is missing
     * @throws org.urlshortener.urlshortener.exceptions.UrlExpired if the alias is expired
     */
    String resolveOriginalUrl(String shortAlias);
}
