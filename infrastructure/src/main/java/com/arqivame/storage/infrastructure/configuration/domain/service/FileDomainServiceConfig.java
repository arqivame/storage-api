package com.arqivame.storage.infrastructure.configuration.domain.service;

import java.nio.file.Path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.domain.file.service.StorageWriter;
import com.arqivame.storage.infrastructure.file.service.FileSystemStorageService;

@Configuration
public class FileDomainServiceConfig {

    private final String rootLocation = "./storage";

    @Bean
    public StorageWriter storageService() {
        return new FileSystemStorageService(Path.of(rootLocation));
    }

}
