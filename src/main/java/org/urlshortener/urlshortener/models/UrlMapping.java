package org.urlshortener.urlshortener.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "url_mapping",
        indexes = {
                @Index(name = "idx_short_url", columnList = "short_url")
        }
)
public class UrlMapping extends BaseModel {

    @Column(name = "long_url", nullable = false, length = 2048)
    String longUrl;

    @Column(name = "short_url", length = 2048)
    String shortUrl;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
