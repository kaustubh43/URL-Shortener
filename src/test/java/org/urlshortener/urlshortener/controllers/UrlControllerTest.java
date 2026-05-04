package org.urlshortener.urlshortener.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.urlshortener.urlshortener.dtos.CreateShortUrlRequest;
import org.urlshortener.urlshortener.exceptions.UrlNotFoundException;
import org.urlshortener.urlshortener.services.UrlService;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlControllerTest {

    @Test
    void shortenUrlUsesServiceAndReturnsAlias() {
        RecordingUrlService urlService = new RecordingUrlService();
        UrlController controller = new UrlController(urlService);

        CreateShortUrlRequest request = new CreateShortUrlRequest();
        request.setLongUrl("https://example.com");

        String response = controller.shortenUrl(request);

        assertEquals("abc", response);
        assertEquals(1, urlService.nonExpiringCalls);
        assertEquals(0, urlService.expiringCalls);
        assertEquals("https://example.com", urlService.lastLongUrl);
        assertNull(urlService.lastExpiresAt);
    }

    @Test
    void resolveLongUrlRedirectsToOriginal() throws UrlNotFoundException {
        RecordingUrlService urlService = new RecordingUrlService();
        urlService.resolvedUrl = "https://example.com";
        UrlController controller = new UrlController(urlService);

        ResponseEntity<Void> response = controller.resolveLongUrl("abc");

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        HttpHeaders headers = response.getHeaders();
        assertEquals("https://example.com", headers.getLocation().toString());
    }

    @Test
    void resolveLongUrlThrowsWhenMissing() {
        RecordingUrlService urlService = new RecordingUrlService();
        urlService.resolvedUrl = null;
        UrlController controller = new UrlController(urlService);

        UrlNotFoundException exception = assertThrows(UrlNotFoundException.class, () -> controller.resolveLongUrl("missing"));
        assertNotNull(exception.getMessage());
    }

    private static class RecordingUrlService implements UrlService {
        private int nonExpiringCalls;
        private int expiringCalls;
        private String lastLongUrl;
        private Instant lastExpiresAt;
        private String resolvedUrl = "abc";

        @Override
        public String shortenUrl(String originalUrl) {
            nonExpiringCalls++;
            lastLongUrl = originalUrl;
            lastExpiresAt = null;
            return "abc";
        }

        @Override
        public String shortenUrl(String originalUrl, Instant expiresAt) {
            expiringCalls++;
            lastLongUrl = originalUrl;
            lastExpiresAt = expiresAt;
            return "abc";
        }

        @Override
        public String resolveOriginalUrl(String shortAlias) {
            return resolvedUrl;
        }
    }
}

