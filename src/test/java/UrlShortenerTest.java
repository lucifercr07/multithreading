import org.example.urlshortener.repository.UrlRepository;
import org.example.urlshortener.service.MD5Generator;
import org.example.urlshortener.service.ShortUrlGenerator;
import org.example.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlShortenerTest {
    @Test
    public void testConcurrentShorteningConsistency() throws InterruptedException {
        UrlRepository urlRepository = new UrlRepository();
        ShortUrlGenerator shortUrlGenerator = new MD5Generator();
        UrlShortenerService urlShortenerService = new UrlShortenerService(urlRepository, shortUrlGenerator);

        int threadCount = 500;
        String testUrl = "https://www.example.com/test";
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        // Store generated codes
        String[] shortCodes = new String[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Thread th = new Thread(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    var shortenedUrl = urlShortenerService.shortenUrl(testUrl);
                    shortCodes[index] = shortenedUrl.getShortUrl();
                } catch (Exception e) {
                    System.out.println("Exception : " + e);
                } finally {
                    doneLatch.countDown();
                }
            });
            th.start();
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();

        var firstShortenedUrl = shortCodes[0];
        for (int i = 1; i < shortCodes.length; ++i) {
            assertEquals(firstShortenedUrl, shortCodes[i],
                    "All threads should get the same short code");
        }

        System.out.println(urlRepository.findAllLongUrls());
    }
}
