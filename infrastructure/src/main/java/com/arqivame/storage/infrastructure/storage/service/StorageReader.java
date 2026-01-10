package com.arqivame.storage.infrastructure.storage.service;

import java.io.InputStream;

@FunctionalInterface
public interface StorageReader {

    InputStream read(
            StorageKey key,
            Long offsetInBytes,
            Long sizeInBytes,
            Long bytesPerSecondsReadRate);

}
