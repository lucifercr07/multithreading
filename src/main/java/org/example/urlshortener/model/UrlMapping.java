package org.example.urlshortener.model;

public class UrlMapping {
    private final String longUrl;
    private final String shortUrl;

    public UrlMapping(String longUrl, String shortUrl) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
