package com.arqivame.storage.infrastructure.commons;

import static java.util.Objects.isNull;

import java.security.MessageDigest;

import com.arqivame.storage.domain.file.Checksum;

public final class MessageDigestUtils {

    private MessageDigestUtils() {
    }

    public static MessageDigest create(final Checksum.Algorithm algorithm) {

        if (isNull(algorithm))
            throw new IllegalArgumentException("Algorithm cannot be null.");

        try {

            final String alg = switch (algorithm) {
                case MD5 -> "MD5";
                case SHA1 -> "SHA-1";
                case SHA256 -> "SHA-256";
            };

            return MessageDigest.getInstance(alg);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create message digest for algorithm: " + algorithm.name(), e);
        }
    }

}
