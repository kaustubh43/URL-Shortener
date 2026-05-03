package org.urlshortener.urlshortener.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.urlshortener.urlshortener.dtos.CreateShortUrlRequest;
import org.urlshortener.urlshortener.services.UrlService;

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public String shortenUrl(@RequestBody CreateShortUrlRequest request) {
        return urlService.shortenUrl(request.getLongUrl());
    }
}
