package com.arqivame.storage.infrastructure.configuration.domain.service;

import java.nio.file.Path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.application.service.storage.FileAssemblerService;
import com.arqivame.storage.application.service.storage.StorageService;
import com.arqivame.storage.infrastructure.storage.service.FileSystemFileAssemblerService;
import com.arqivame.storage.infrastructure.storage.service.FileSystemStorageService;

@Configuration
public class FileDomainServiceConfig {

    private final String rootLocation = "./storage";

    @Bean
    StorageService storageService() {
        return new FileSystemStorageService(Path.of(rootLocation));
    }

    @Bean
    FileAssemblerService chunkMergeService() {
        return new FileSystemFileAssemblerService(Path.of(rootLocation));
    }

}
