package com.arqivame.storage.infrastructure.configuration.application.port;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.port.ChunkWriter;
import com.arqivame.storage.infrastructure.storage.service.ChunkStorageWriter;
import com.arqivame.storage.infrastructure.storage.service.StorageWriter;

@Configuration
public class FilePortsConfig {

    private final StorageWriter storageWriter;

    public FilePortsConfig(final StorageWriter storageWriter) {
        this.storageWriter = Objects.requireNonNull(storageWriter);
    }

    @Bean
    ChunkWriter chunkWriterPort() {
        return new ChunkStorageWriter(storageWriter);
    }

}
