package org.example.urlshortener.repository;

import org.example.urlshortener.model.UrlMapping;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlRepository {
    private final Map<String, String> shortToLong = new ConcurrentHashMap<>();
    private final Map<String, String> longToShort = new ConcurrentHashMap<>();

    public boolean saveUrlMapping(UrlMapping urlMapping) {
        shortToLong.put(urlMapping.getShortUrl(), urlMapping.getLongUrl());
        longToShort.put(urlMapping.getLongUrl(), urlMapping.getShortUrl());

        return true;
    }

    public String getUrl(final String shortUrl) {
        return shortToLong.get(shortUrl);
    }

    public String findByOriginalUrl(final String url) {
        return longToShort.get(url);
    }

    public Iterable<String> findAllLongUrls() {
        return new ArrayList<>(shortToLong.values());
    }
}
