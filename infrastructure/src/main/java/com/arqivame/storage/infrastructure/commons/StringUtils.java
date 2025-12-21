package com.arqivame.storage.infrastructure.commons;

import org.apache.commons.codec.binary.Hex;

public final class StringUtils {

    private StringUtils() {
    }

    public static String toHexString(final byte[] data) {
        return Hex.encodeHexString(data);
    }

}
