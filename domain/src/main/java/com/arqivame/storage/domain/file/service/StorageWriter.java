package com.arqivame.storage.domain.file.service;

import java.io.InputStream;

import com.arqivame.storage.domain.file.Checksum;

@FunctionalInterface
public interface StorageWriter {

    Checksum write(
            StorageKey key,
            InputStream inputStream,
            Long sizeInBytes,
            Long bytesPerSecondsWrittenRate,
            Checksum.Algorithm checksumAlgorithm);

}
