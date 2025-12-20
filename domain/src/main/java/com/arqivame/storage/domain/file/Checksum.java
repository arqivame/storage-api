package com.arqivame.storage.domain.file;

public record Checksum(String value, Algorithm algorithm) {

    public static Checksum from(final String value, final Algorithm algorithm) {
        return new Checksum(value, algorithm);
    }

    // TODO: implementation to calculate checksum
    // public static Checksum of(final byte[] value, final Algorithm algorithm) {
    // return new Checksum(new String(value), algorithm);
    // }

    public enum Algorithm {
        MD5,
        SHA1,
        SHA256
    }

}
