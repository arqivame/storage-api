package com.arqivame.storage.application.service.storage;

import java.io.InputStream;

@FunctionalInterface
public interface StorageReader {

    InputStream read(
            StorageKey key,
            Long offsetInBytes,
            Long sizeInBytes,
            Long bytesPerSecondsReadRate);

}
