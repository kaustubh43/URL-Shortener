package org.urlshortener.urlshortener.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.urlshortener.urlshortener.dtos.CreateShortUrlRequest;
import org.urlshortener.urlshortener.exceptions.UrlNotFoundException;
import org.urlshortener.urlshortener.services.UrlService;

import java.net.URI;

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public String shortenUrl(@RequestBody CreateShortUrlRequest request) {
        if (request.getExpiresAt() == null) {
            return urlService.shortenUrl(request.getLongUrl());
        } else if (request.getCustomUrl() == null) {
            return urlService.shortenUrl(request.getLongUrl(), request.getExpiresAt());
        } else {
            return urlService.shortenUrl(request.getLongUrl(), request.getCustomUrl(), request.getExpiresAt());
        }
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> resolveLongUrl(@PathVariable String shortUrl) throws UrlNotFoundException {
        String resolvedUrl = urlService.resolveOriginalUrl(shortUrl);
        if(resolvedUrl == null) {
            throw new UrlNotFoundException("Url not resolved for: " + shortUrl);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(resolvedUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
