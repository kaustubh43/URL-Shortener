package org.urlshortener.urlshortener.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.urlshortener.urlshortener.models.UrlMapping;
import org.urlshortener.urlshortener.repositories.UrlMappingRepository;
import org.urlshortener.urlshortener.utils.shortener.Base62Util;
import org.urlshortener.urlshortener.utils.shortener.IdScramblerUtil;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private Base62Util base62Util;

    @Mock
    private IdScramblerUtil idScramblerUtil;

    @Mock
    private UrlMappingRepository urlMappingRepository;

    private UrlServiceImpl urlService;

    @BeforeEach
    void setUp() {
        urlService = new UrlServiceImpl(base62Util, idScramblerUtil, urlMappingRepository);
    }

    @Test
    void shortenUrlPersistsAndReturnsAlias() {
        String longUrl = "https://example.com";
        Instant expiresAt = Instant.now().plusSeconds(600);

        UrlMapping savedMapping = new UrlMapping();
        savedMapping.setId(42L);

        when(urlMappingRepository.save(any(UrlMapping.class))).thenReturn(savedMapping);
        when(idScramblerUtil.scramble(42L)).thenReturn(100L);
        when(base62Util.encode(100L)).thenReturn("abc");

        String alias = urlService.shortenUrl(longUrl, expiresAt);

        assertEquals("abc", alias);

        ArgumentCaptor<UrlMapping> captor = ArgumentCaptor.forClass(UrlMapping.class);
        verify(urlMappingRepository, times(2)).save(captor.capture());

        UrlMapping initialSave = captor.getAllValues().get(0);
        assertEquals(longUrl, initialSave.getLongUrl());
        assertEquals(expiresAt, initialSave.getExpiresAt());

        UrlMapping secondSave = captor.getAllValues().get(1);
        assertEquals("abc", secondSave.getShortUrl());
    }

    @Test
    void resolveOriginalUrlReturnsLongUrlWhenNotExpired() {
        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl("https://example.com");
        mapping.setExpiresAt(Instant.now().plusSeconds(300));

        when(urlMappingRepository.findByShortUrl("abc")).thenReturn(Optional.of(mapping));

        assertEquals("https://example.com", urlService.resolveOriginalUrl("abc"));
    }

    @Test
    void resolveOriginalUrlReturnsNullWhenExpired() {
        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl("https://example.com");
        mapping.setExpiresAt(Instant.now().minusSeconds(300));

        when(urlMappingRepository.findByShortUrl("abc")).thenReturn(Optional.of(mapping));

        assertNull(urlService.resolveOriginalUrl("abc"));
    }

    @Test
    void resolveOriginalUrlReturnsNullWhenMissing() {
        when(urlMappingRepository.findByShortUrl("missing")).thenReturn(Optional.empty());

        assertNull(urlService.resolveOriginalUrl("missing"));
    }
}

