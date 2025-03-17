package org.example.urlshortener.service;

public class MD5Generator implements ShortUrlGenerator {
    private static final int CODE_LENGTH = 6;

    @Override
    public String generateCode(String url) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(url.getBytes());

            // Convert to Base64 and take first 6 chars
            String base64 = java.util.Base64.getUrlEncoder().encodeToString(digest);
            return base64.substring(0, CODE_LENGTH);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate short code", e);
        }
    }
}
