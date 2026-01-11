package com.arqivame.storage.infrastructure.configuration.service;

import java.nio.file.Path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.infrastructure.file.service.FileAssembler;
import com.arqivame.storage.infrastructure.file.service.filesystem.FileSystemFileAssembler;
import com.arqivame.storage.infrastructure.storage.service.StorageService;
import com.arqivame.storage.infrastructure.storage.service.filesystem.FileSystemStorageService;

@Configuration
public class StorageServiceConfig {

    private final String rootLocation = "./storage";

    @Bean
    StorageService storageService() {
        return new FileSystemStorageService(Path.of(rootLocation));
    }

    @Bean
    FileAssembler chunkMergeService() {
        return new FileSystemFileAssembler(Path.of(rootLocation));
    }

}
