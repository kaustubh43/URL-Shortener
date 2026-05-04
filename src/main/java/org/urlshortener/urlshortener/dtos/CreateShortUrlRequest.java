package org.urlshortener.urlshortener.dtos;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortUrlRequest {
    String longUrl;

    @Nullable
    Instant expiresAt;

    @Nullable
    String customUrl;
}
