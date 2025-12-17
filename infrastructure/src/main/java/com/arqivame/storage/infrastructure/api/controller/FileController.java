package com.arqivame.storage.infrastructure.api.controller;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arqivame.storage.application.file.session.chunk.upload.UploadSessionChunkInput;
import com.arqivame.storage.application.file.session.chunk.upload.UploadSessionChunkUseCase;
import com.arqivame.storage.infrastructure.api.FileAPI;

@RestController
public class FileController implements FileAPI {

    private final UploadSessionChunkUseCase uploadSessionChunkUseCase;

    public FileController(
            final UploadSessionChunkUseCase uploadSessionChunkUseCase) {
        this.uploadSessionChunkUseCase = Objects.requireNonNull(uploadSessionChunkUseCase);
    }

    @Override
    public ResponseEntity<Void> uploadChunk(final MultipartFile chunk, final JwtAuthenticationToken authentication) {

        uploadSessionChunkUseCase.execute(new UploadSessionChunkInput(null, null, null));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
