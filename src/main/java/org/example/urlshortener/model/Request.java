package org.example.urlshortener.model;

public class Request {
    private final String longUrl;

    public Request(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
