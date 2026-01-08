package com.arqivame.storage.infrastructure.api.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arqivame.storage.application.usecase.file.chunk.upload.UploadChunkUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.abort.AbortUploadSessionInput;
import com.arqivame.storage.application.usecase.file.session.upload.abort.AbortUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.create.CreateUploadSessionInput;
import com.arqivame.storage.application.usecase.file.session.upload.create.CreateUploadSessionUseCase;
import com.arqivame.storage.domain.file.Checksum;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("test/files")
public class TestController {

    private final CreateUploadSessionUseCase createUploadSessionUseCase;
    private final UploadChunkUseCase writeUploadSessionChunkUseCase;
    private final AbortUploadSessionUseCase cancelUploadSessionUseCase;

    public TestController(
            CreateUploadSessionUseCase createUploadSessionUseCase,
            UploadChunkUseCase writeUploadSessionChunkUseCase,
            AbortUploadSessionUseCase cancelUploadSessionUseCase) {
        this.createUploadSessionUseCase = createUploadSessionUseCase;
        this.writeUploadSessionChunkUseCase = writeUploadSessionChunkUseCase;
        this.cancelUploadSessionUseCase = cancelUploadSessionUseCase;
    }

    @PutMapping(value = "{fileId}/sessions/{sessionId}/chunks/{chunkPart}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> uploadPart(
            @PathVariable UUID fileId,
            @PathVariable UUID sessionId,
            @PathVariable Long chunkPart,
            @RequestHeader("X-Checksum-Value") String checksumValue,
            @RequestHeader("X-Checksum-Algorithm") Checksum.Algorithm checksumAlgorithm,
            HttpServletRequest request) throws IOException {

        // writeUploadSessionChunkUseCase
        //         .execute(new UploadChunkInput(
        //                 fileId,
        //                 sessionId,
        //                 checksumValue,
        //                 checksumAlgorithm,
        //                 request.getInputStream(),
        //                 chunkPart));

        return ResponseEntity.ok().build();

    }

    @PostMapping(path = "sessions")
    public ResponseEntity<Object> createUploadSession(@RequestBody CreateUploadSessionInput input) {
        return ResponseEntity.ok(createUploadSessionUseCase.execute(input));
    }

    @Transactional
    @PostMapping("sessions/cancel")
    public ResponseEntity<Void> cancel(@RequestBody AbortUploadSessionInput input) {

        cancelUploadSessionUseCase.execute(input);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "{fileId}/sessions/{sessionId}/complete")
    public ResponseEntity<?> completeSession(
            @PathVariable UUID fileId,
            @PathVariable UUID sessionId) throws IOException {

        // initiateUploadSessionProcessingUseCase.execute(new InitiateUploadSessionProcessingInput(fileId, sessionId));

        return ResponseEntity.ok().build();

    }

}
