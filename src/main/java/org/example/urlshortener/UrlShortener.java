package org.example.urlshortener;

import org.example.urlshortener.repository.UrlRepository;
import org.example.urlshortener.service.MD5Generator;
import org.example.urlshortener.service.ShortUrlGenerator;
import org.example.urlshortener.service.UrlShortenerService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UrlShortener {
    public static void main(String[] args) {
        UrlRepository urlRepository = new UrlRepository();
        ShortUrlGenerator shortUrlGenerator = new MD5Generator();
        UrlShortenerService urlShortenerService = new UrlShortenerService(urlRepository, shortUrlGenerator);

//        var shortUrl = urlShortenerService.shortenUrl("https://google.com").getShortUrl();
//        System.out.println("Shortened Url = " + shortUrl);
//        System.out.println("Long Url = " + urlShortenerService.getUrl(shortUrl));

        runBenchmark(urlShortenerService);
    }

    private static void runBenchmark(UrlShortenerService urlShortenerService) {
        List<String> mockUrls = List.of("https://www.example.com/page1","https://www.example.com/page1","https://www.google.com/search?q=java","https://www.youtube.com/watch?v=dQw4w9WgXcQ","https://github.com/user/repo","https://www.amazon.com/product/B01N1SE4EP","https://www.stackoverflow.com/questions/123456","https://en.wikipedia.org/wiki/Main_Page","https://www.reddit.com/r/programming","https://www.twitter.com/elonmusk","https://www.linkedin.com/in/billgates","https://www.nasa.gov/image-of-the-day","https://www.bbc.com/news","https://www.nytimes.com/","https://www.medium.com/@author/article","https://www.twitch.tv/channel","https://www.instagram.com/nationalgeographic","https://www.apple.com/iphone","https://developer.mozilla.org/en-US/docs/Web/JavaScript","https://www.oracle.com/java/technologies/javase-downloads.html","https://www.jetbrains.com/idea/");
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();
        mockUrls.forEach(url -> {
            executorService.submit(() -> {
                try {
                    var shortenedUrl = urlShortenerService.shortenUrl(url);
                    if (shortenedUrl != null) {
                        successCount.incrementAndGet();
                        System.out.println("Shortened Url = " + shortenedUrl.getShortUrl());
                        System.out.println("Long Url = " + urlShortenerService.getUrl(shortenedUrl.getShortUrl()));
                    }
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                }
            });
        });

        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("failureCount = " + failureCount);
        System.out.println("successCount = " + successCount);
    }
}
