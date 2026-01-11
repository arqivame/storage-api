package com.arqivame.storage.infrastructure.configuration.application.port;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.port.ChunkReader;
import com.arqivame.storage.application.port.ChunkWriter;
import com.arqivame.storage.infrastructure.storage.service.ChunkStorageReader;
import com.arqivame.storage.infrastructure.storage.service.ChunkStorageWriter;
import com.arqivame.storage.infrastructure.storage.service.StorageReader;
import com.arqivame.storage.infrastructure.storage.service.StorageWriter;

@Configuration
public class FilePortsConfig {

    private final StorageWriter storageWriter;
    private final StorageReader storageReader;

    public FilePortsConfig(final StorageWriter storageWriter, final StorageReader storageReader) {
        this.storageWriter = Objects.requireNonNull(storageWriter);
        this.storageReader = Objects.requireNonNull(storageReader);
    }

    @Bean
    ChunkWriter chunkWriterPort() {
        return new ChunkStorageWriter(storageWriter);
    }

    @Bean
    ChunkReader chunkReaderPort() {
        return new ChunkStorageReader(storageReader);
    }

}
