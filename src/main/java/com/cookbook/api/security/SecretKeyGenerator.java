package com.cookbook.api.security;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Data
@Component
public class SecretKeyGenerator {

    private String secretKey;

    public SecretKeyGenerator() {
        generateSecretKey();
    }

    private void generateSecretKey() {
        byte[] keyBytes = generateRandomBytes(32);
        secretKey = encodeBase64(keyBytes);
    }

    private byte[] generateRandomBytes(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    private String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
