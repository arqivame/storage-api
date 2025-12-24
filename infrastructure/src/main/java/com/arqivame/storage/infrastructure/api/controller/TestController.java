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

import com.arqivame.storage.application.file.session.cancel.CancelUploadSessionUseCase;
import com.arqivame.storage.application.file.session.cancel.CancelUploadSessionUseCaseInput;
import com.arqivame.storage.application.file.session.chunk.write.WriteUploadSessionChunkInput;
import com.arqivame.storage.application.file.session.chunk.write.WriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionInput;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionUseCase;
import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.domain.file.FileGateway;
import com.arqivame.storage.domain.file.FileID;
import com.arqivame.storage.domain.file.UploadSessionID;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("test/files")
public class TestController {

    private final CreateUploadSessionUseCase createUploadSessionUseCase;
    private final WriteUploadSessionChunkUseCase writeUploadSessionChunkUseCase;
    private final CancelUploadSessionUseCase cancelUploadSessionUseCase;

    private final FileGateway fileGateway;

    public TestController(
            CreateUploadSessionUseCase createUploadSessionUseCase,
            WriteUploadSessionChunkUseCase writeUploadSessionChunkUseCase,
            CancelUploadSessionUseCase cancelUploadSessionUseCase,
            FileGateway fileGateway) {
        this.createUploadSessionUseCase = createUploadSessionUseCase;
        this.writeUploadSessionChunkUseCase = writeUploadSessionChunkUseCase;
        this.cancelUploadSessionUseCase = cancelUploadSessionUseCase;
        this.fileGateway = fileGateway;
    }

    @PutMapping(value = "{fileId}/sessions/{sessionId}/chunks/{chunkPart}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> uploadPart(
            @PathVariable UUID fileId,
            @PathVariable UUID sessionId,
            @PathVariable Long chunkPart,
            @RequestHeader("X-Checksum-Value") String checksumValue,
            @RequestHeader("X-Checksum-Algorithm") Checksum.Algorithm checksumAlgorithm,
            HttpServletRequest request) throws IOException {

        writeUploadSessionChunkUseCase
                .execute(new WriteUploadSessionChunkInput(
                        fileId,
                        sessionId,
                        checksumValue,
                        checksumAlgorithm,
                        request.getInputStream(),
                        chunkPart));

        return ResponseEntity.ok().build();

    }

    @PostMapping(path = "sessions")
    public ResponseEntity<Object> createUploadSession(@RequestBody CreateUploadSessionInput input) {
        return ResponseEntity.ok(createUploadSessionUseCase.execute(input));
    }

    @Transactional
    @PostMapping("sessions/cancel")
    public ResponseEntity<Void> cancel(@RequestBody CancelUploadSessionUseCaseInput input) {

        cancelUploadSessionUseCase.execute(input);

        return ResponseEntity.ok().build();
    }

}
