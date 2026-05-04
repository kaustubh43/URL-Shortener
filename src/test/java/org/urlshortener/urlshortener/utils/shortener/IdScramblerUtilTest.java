package org.urlshortener.urlshortener.utils.shortener;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdScramblerUtilTest {

    private final IdScramblerUtil idScramblerUtil = new IdScramblerUtil();

    @Test
    void scrambleAndUnscrambleRoundTrip() {
        long[] values = {0L, 1L, 2L, 1234L, 987654321L, (1L << 39) - 1};
        for (long value : values) {
            long scrambled = idScramblerUtil.scramble(value);
            long unscrambled = idScramblerUtil.unscramble(scrambled);
            assertEquals(value, unscrambled);
        }
    }
}

