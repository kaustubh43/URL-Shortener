# URL Shortener
> A Spring Boot backend for shortening URLs with optional expiration and MySQL persistence.

## Overview
This service creates short aliases for long URLs and redirects requests to the original URL. Expiration is optional and uses ISO-8601 timestamps (UTC).

## Features (Current)
- Shorten a URL
- Resolve a short URL via HTTP 302 redirect
- Optional expiration timestamp

## Tech Stack
- Java 26
- Spring Boot 4 (WebMVC, Data JPA)
- MySQL
- Lombok

## API
### Shorten URL
- `POST /`
- Request body:
```json
{
  "longUrl": "https://example.com/some/very/long/url",
  "expiresAt": "2026-05-04T20:26:59Z"
}
```
- Response body (string):
```
abc123
```

### Resolve Short URL
- `GET /{shortUrl}`
- Response: `302 FOUND` with `Location` header pointing to the original URL

## Project Setup
1. Ensure MySQL is running and accessible.
2. Configure database settings in `src/main/resources/application.properties`.
3. Run the application.

```zsh
./mvnw spring-boot:run
```

### Run Tests
```zsh
./mvnw test
```

## Future Scope
- Custom alias support
- URL deletion and update endpoints
- User authentication and ownership
- Click analytics and rate limiting
- Expiration management UI and cleanup jobs
