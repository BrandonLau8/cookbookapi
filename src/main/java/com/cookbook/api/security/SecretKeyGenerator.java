package com.cookbook.api.security;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {

    public byte[] keyBytes = generateRandomBytes(32);
    public String secretKey = encodeBase64(keyBytes);

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
