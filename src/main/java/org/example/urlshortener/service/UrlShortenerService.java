package org.example.urlshortener.service;

import org.example.urlshortener.model.UrlMapping;
import org.example.urlshortener.repository.UrlRepository;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UrlShortenerService {
    private UrlRepository urlRepository;
    private final ShortUrlGenerator urlGenerator;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public UrlShortenerService(UrlRepository urlRepository, ShortUrlGenerator urlGenerator) {
        this.urlRepository = urlRepository;
        this.urlGenerator = urlGenerator;
    }

    public UrlMapping shortenUrl(final String url) {
        lock.writeLock().lock();
        try {
            var shortenedUrl = urlRepository.findByOriginalUrl(url);
            if (shortenedUrl != null) {
                return new UrlMapping(url, shortenedUrl);
            }

            shortenedUrl = urlGenerator.generateCode(url);
            var urlMapping = new UrlMapping(url, shortenedUrl);
            urlRepository.saveUrlMapping(urlMapping);

            return urlMapping;
        } catch (Exception e) {
            throw new RuntimeException("Failure to shorten URL");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getUrl(final String shortenedUrl) {
        return urlRepository.getUrl(shortenedUrl);
    }
}
