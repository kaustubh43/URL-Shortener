package org.urlshortener.urlshortener.services;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UrlServiceImpl implements UrlService {

    @Override
    public String shortenUrl(String originalUrl) {
        return "";
    }

    @Override
    public String shortenUrl(String originalUrl, Instant expiresAt) {
        return "";
    }

    @Override
    public String resolveOriginalUrl(String shortAlias) {
        return "";
    }
}
