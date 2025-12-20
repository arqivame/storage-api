package com.arqivame.storage.infrastructure.api.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.arqivame.storage.application.file.session.create.CreateUploadSessionInput;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionUseCase;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.infrastructure.api.FileAPI;

// @RestController
public class FileController implements FileAPI {

    private final CreateUploadSessionUseCase createUploadSessionUseCase;

    // private final UploadSessionChunkUseCase uploadSessionChunkUseCase;

    // public FileController(
    // final UploadSessionChunkUseCase uploadSessionChunkUseCase) {
    // this.uploadSessionChunkUseCase =
    // Objects.requireNonNull(uploadSessionChunkUseCase);
    // }

    public FileController(final CreateUploadSessionUseCase createUploadSessionUseCase) {

        this.createUploadSessionUseCase = Objects.requireNonNull(createUploadSessionUseCase);
    }

    @Override
    public ResponseEntity<Object> createUploadSession(final UUID fileId) {

        final var input = new CreateUploadSessionInput(
                fileId,
                null,
                Duration.ofHours(1),
                1,
                "123",
                Checksum.Algorithm.MD5);

        createUploadSessionUseCase.execute(input);

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUploadSession'");
    }

    @Override
    public ResponseEntity<Void> uploadChunk(final MultipartFile chunk, final JwtAuthenticationToken authentication) {

        // uploadSessionChunkUseCase.execute(new UploadSessionChunkInput(null, null,
        // null));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/testThrotle")
    public ResponseEntity<Void> throtle(
            final @RequestParam("file") MultipartFile chunk,
            final @RequestParam("fileId") UUID fileId,
            final @RequestParam("sessionId") UUID sessionId,
            final @RequestParam("chunkIndex") Integer chunkIndex) throws IOException {

        // ThrottledInputStream throttledInputStream = new ThrottledInputStream(
        // chunk.getInputStream(),
        // 100 * 1024 // Limite de 100 KB/s
        // );

        // final var aham = new UploadSessionChunkInput(fileId, sessionId, chunkInput);

        // uploadSessionChunkUseCase.execute(aham);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/test")
    public ResponseEntity<Void> uploadChunk(
            final @RequestParam("file") MultipartFile chunk,
            final @RequestParam("fileId") UUID fileId,
            final @RequestParam("sessionId") UUID sessionId,
            final @RequestParam("chunkIndex") Integer chunkIndex) throws IOException {

        // final UploadSessionChunkInput.Chunk chunkInput = new
        // UploadSessionChunkInput.Chunk(
        // chunk.getInputStream(),
        // chunkIndex);

        // final var aham = new UploadSessionChunkInput(fileId, sessionId, chunkInput);

        // uploadSessionChunkUseCase.execute(aham);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
