package org.urlshortener.urlshortener.services;

import org.springframework.stereotype.Component;
import org.urlshortener.urlshortener.models.UrlMapping;
import org.urlshortener.urlshortener.repositories.UrlMappingRepository;
import org.urlshortener.urlshortener.utils.shortener.Base62Util;
import org.urlshortener.urlshortener.utils.shortener.IdScramblerUtil;

import java.time.Instant;

@Component
public class UrlServiceImpl implements UrlService {

    private Base62Util base62Util;
    private IdScramblerUtil idScramblerUtil;
    private UrlMappingRepository urlMappingRepository;

    public UrlServiceImpl(Base62Util base62Util, IdScramblerUtil idScramblerUtil, UrlMappingRepository urlMappingRepository) {
        this.base62Util = base62Util;
        this.idScramblerUtil = idScramblerUtil;
        this.urlMappingRepository = urlMappingRepository;
    }

    @Override
    public String shortenUrl(String originalUrl) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(originalUrl);

        // Save and get database id.
        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        long dbId = savedUrlMapping.getId();

        // Scramble
        long scrambledId = idScramblerUtil.scramble(dbId);
        String shortAlias = base62Util.encode(scrambledId);

        // Set and save in the database.
        savedUrlMapping.setShortUrl(shortAlias);
        urlMappingRepository.save(savedUrlMapping);

        return shortAlias;
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
