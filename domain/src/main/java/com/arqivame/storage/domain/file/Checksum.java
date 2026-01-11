package com.arqivame.storage.domain.file;

import com.arqivame.storage.domain.ValueObject;

public record Checksum(String value, Algorithm algorithm) implements ValueObject {

    public static Checksum from(final String value, final Algorithm algorithm) {
        return new Checksum(value, algorithm);
    }

    public Boolean equals(final Checksum other) {
        return this.value.equals(other.value) && this.algorithm == other.algorithm;
    }

    public enum Algorithm {
        MD5,
        SHA1,
        SHA256
    }

}
