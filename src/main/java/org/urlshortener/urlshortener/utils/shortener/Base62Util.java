package org.urlshortener.urlshortener.utils.shortener;

import org.springframework.stereotype.Component;

@Component
public class Base62Util {

    // Our 62 characters. The order matters! If you change this later, all old links break.
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();

    /**
     * Converts a Base10 database ID into a Base62 string.
     */
    public String encode(long id) {
        // Edge case: if the ID is 0, our loop won't run.
        if (id == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }

        StringBuilder shortUrl = new StringBuilder();

        while (id > 0) {
            int remainder = (int) (id % BASE);
            shortUrl.append(ALPHABET.charAt(remainder));
            id = id / BASE;
        }

        // We appended characters from right to left, so we must reverse the result
        return shortUrl.reverse().toString();
    }

    /**
     * Converts a Base62 string back into a Base10 database ID.
     */
    public long decode(String shortUrl) {
        long id = 0;
        
        for (int i = 0; i < shortUrl.length(); i++) {
            char c = shortUrl.charAt(i);
            int value = ALPHABET.indexOf(c);
            
            // Defensive programming: Catch invalid characters immediately
            if (value == -1) {
                throw new IllegalArgumentException("Invalid character in short URL: " + c);
            }
            
            id = id * BASE + value;
        }
        
        return id;
    }
}