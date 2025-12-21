package com.arqivame.storage.infrastructure.commons;

import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.time.temporal.ChronoUnit;

import org.apache.commons.io.input.BoundedInputStream;
import org.apache.commons.io.input.ThrottledInputStream;

public final class InputStreamUtils {

    private InputStreamUtils() {
    }

    public static InputStream throttled(
            final InputStream inputStream,
            final Long bytesPerSecondsWrittenRate) {
        try {
            return ThrottledInputStream
                    .builder()
                    .setInputStream(inputStream)
                    .setMaxBytes(bytesPerSecondsWrittenRate, ChronoUnit.SECONDS)
                    .get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create throttled input stream.", e);
        }
    }

    public static InputStream bounded(
            final InputStream inputStream,
            final Long sizeInBytes) {
        try {
            return BoundedInputStream
                    .builder()
                    .setInputStream(inputStream)
                    .setMaxCount(sizeInBytes)
                    .get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bounded input stream.", e);
        }
    }

    public static InputStream digestible(
            final InputStream inputStream,
            final MessageDigest messageDigest) {
        try {
            return new DigestInputStream(inputStream, messageDigest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create digest input stream.", e);
        }
    }

}
