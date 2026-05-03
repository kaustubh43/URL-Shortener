package org.urlshortener.urlshortener.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.urlshortener.urlshortener.services.UrlService;

@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }
}
