package com.arqivame.storage.infrastructure.configuration.domain.service;

import java.nio.file.Path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arqivame.storage.domain.file.service.UploadSessionChunksWriter;
import com.arqivame.storage.infrastructure.file.service.FileSystemUploadSessionChunksWriter;

@Configuration
public class FileDomainServiceConfig {

    private final String rootLocation = "./storage";

    @Bean
    public UploadSessionChunksWriter uploadSessionChunksWriter() {
        return new FileSystemUploadSessionChunksWriter(Path.of(rootLocation));
    }

}
