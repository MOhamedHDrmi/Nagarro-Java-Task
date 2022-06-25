package com.nagarro.javatest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Utils {

    Logger logger = LoggerFactory.getLogger(Utils.class);

    public String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            return byteToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error While Hashing Account Number", e);
            return base;
        }
    }

    private String byteToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);

			if (hex.length() == 1)
				hexString.append('0');

            hexString.append(hex);
        }

        return hexString.toString();
    }


}
