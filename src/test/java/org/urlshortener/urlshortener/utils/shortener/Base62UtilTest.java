package org.urlshortener.urlshortener.utils.shortener;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Base62UtilTest {

    private final Base62Util base62Util = new Base62Util();

    @Test
    void encodeDecodeRoundTrip() {
        long id = 123456789L;
        String encoded = base62Util.encode(id);
        long decoded = base62Util.decode(encoded);
        assertEquals(id, decoded);
    }

    @Test
    void encodeZeroReturnsFirstAlphabetChar() {
        assertEquals("a", base62Util.encode(0));
    }

    @Test
    void decodeRejectsInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> base62Util.decode("abc$"));
    }
}
