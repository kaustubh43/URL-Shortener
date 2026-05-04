package org.urlshortener.urlshortener.services;

import org.springframework.stereotype.Component;
import org.urlshortener.urlshortener.exceptions.UrlAlreadyExists;
import org.urlshortener.urlshortener.exceptions.UrlExpired;
import org.urlshortener.urlshortener.models.UrlMapping;
import org.urlshortener.urlshortener.repositories.UrlMappingRepository;
import org.urlshortener.urlshortener.utils.shortener.Base62Util;
import org.urlshortener.urlshortener.utils.shortener.IdScramblerUtil;

import java.time.Instant;
import java.util.Optional;

@Component
public class UrlServiceImpl implements UrlService {

    private final Base62Util base62Util;
    private final IdScramblerUtil idScramblerUtil;
    private final UrlMappingRepository urlMappingRepository;

    public UrlServiceImpl(Base62Util base62Util, IdScramblerUtil idScramblerUtil, UrlMappingRepository urlMappingRepository) {
        this.base62Util = base62Util;
        this.idScramblerUtil = idScramblerUtil;
        this.urlMappingRepository = urlMappingRepository;
    }

    @Override
    public String shortenUrl(String originalUrl) {
        return shortenUrl(originalUrl, null);
    }

    @Override
    public String shortenUrl(String originalUrl, Instant expiresAt) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(originalUrl);
        urlMapping.setExpiresAt(expiresAt);

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
    public String resolveOriginalUrl(String shortAlias) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortUrl(shortAlias);
        return optionalUrlMapping
                .filter(urlMapping -> checkExpiryDate(urlMapping.getExpiresAt()))
                .map(UrlMapping::getLongUrl)
                .orElse(null);
    }

    @Override
    public String shortenUrl(String longUrl, String customUrl, Instant expiresAt) {
        Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findByShortUrl(customUrl);
        if(optionalUrlMapping.isPresent()) {
            throw new UrlAlreadyExists("URL is already taken, choose another custom URL");
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setLongUrl(longUrl);
        urlMapping.setShortUrl(customUrl);
        urlMapping.setExpiresAt(expiresAt);

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return savedUrlMapping.getShortUrl();
    }

    /*
     * Checks expiry date
     * If null then return True or
     * @throws: UrlExpired when url is expired.
     */
    public static boolean checkExpiryDate(Instant expiryDate) {
        if (expiryDate != null && Instant.now().isAfter(expiryDate)) {
            throw new UrlExpired("URL has expired");
        }
        return true;
    }
}
