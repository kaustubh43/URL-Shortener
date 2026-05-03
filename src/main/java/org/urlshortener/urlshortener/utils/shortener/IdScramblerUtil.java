package org.urlshortener.urlshortener.utils.shortener;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class IdScramblerUtil {

    // 40 bits gives us ~1.1 trillion IDs. Using bitwise AND is faster than modulo (%)
    private static final long MASK = (1L << 40) - 1; 
    
    // A random large odd number. Change this and your links change completely!
    private static final long KEY = 1203091910245L; 
    
    // A random offset to push early IDs (like 1, 2, 3) deep into the number space
    private static final long OFFSET = 8192381239L; 
    
    private static final long INVERSE_KEY;

    // This block runs once when the JVM loads the class
    static {
        BigInteger key = BigInteger.valueOf(KEY);
        // Our modulus is 2^40
        BigInteger mod = BigInteger.valueOf(1L).shiftLeft(40); 
        // Calculate the mathematical mirror to reverse the scrambling
        INVERSE_KEY = key.modInverse(mod).longValue();
    }

    /**
     * Takes a sequential database ID and scatters it.
     */
    public long scramble(long id) {
        return ((id + OFFSET) * KEY) & MASK;
    }

    /**
     * Takes a scattered ID and perfectly reconstructs the original sequential database ID.
     */
    public long unscramble(long scrambled) {
        long unscrambledWithOffset = (scrambled * INVERSE_KEY) & MASK;
        // Subtract the offset, and use bitwise AND again to handle negative wrapping
        return (unscrambledWithOffset - OFFSET) & MASK; 
    }
}