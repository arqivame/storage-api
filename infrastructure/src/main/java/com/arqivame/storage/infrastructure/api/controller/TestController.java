package com.arqivame.storage.infrastructure.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.arqivame.storage.application.usecase.file.chunk.download.DownloadChunkInput;
import com.arqivame.storage.application.usecase.file.chunk.download.DownloadChunkUseCase;
import com.arqivame.storage.application.usecase.file.chunk.upload.UploadChunkInput;
import com.arqivame.storage.application.usecase.file.chunk.upload.UploadChunkUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.abort.AbortUploadSessionInput;
import com.arqivame.storage.application.usecase.file.session.upload.abort.AbortUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.complete.CompleteUploadSessionInput;
import com.arqivame.storage.application.usecase.file.session.upload.complete.CompleteUploadSessionUseCase;
import com.arqivame.storage.application.usecase.file.session.upload.create.CreateUploadSessionInput;
import com.arqivame.storage.application.usecase.file.session.upload.create.CreateUploadSessionUseCase;
import com.arqivame.storage.domain.file.Checksum;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("test/files")
public class TestController {

    private final CompleteUploadSessionUseCase completeUploadSessionUseCase;
    private final CreateUploadSessionUseCase createUploadSessionUseCase;
    private final UploadChunkUseCase uploadChunkUseCase;
    private final AbortUploadSessionUseCase abortUploadSessionUseCase;
    // private final DownloadChunkUseCase downloadChunkUseCase;

    public TestController(
            CompleteUploadSessionUseCase completeUploadSessionUseCase,
            CreateUploadSessionUseCase createUploadSessionUseCase,
            UploadChunkUseCase uploadChunkUseCase,
            AbortUploadSessionUseCase abortUploadSessionUseCase) {
        this.completeUploadSessionUseCase = completeUploadSessionUseCase;
        this.createUploadSessionUseCase = createUploadSessionUseCase;
        this.uploadChunkUseCase = uploadChunkUseCase;
        this.abortUploadSessionUseCase = abortUploadSessionUseCase;
        // this.downloadChunkUseCase = downloadChunkUseCase;
    }

    @GetMapping("{fileId}/upload-session/chunks/{chunkIndex}")
    public ResponseEntity<StreamingResponseBody> streamFile(
            @PathVariable UUID fileId,
            @PathVariable Long chunkIndex) {

        InputStream inputStream = null;
        // downloadChunkUseCase
        //         .execute(new DownloadChunkInput(fileId, chunkIndex))
        //         .inputStream();

        StreamingResponseBody responseBody = outputStream -> {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
            }
        };

        return ResponseEntity.ok()
                .body(responseBody);
    }

    @PutMapping(value = "{fileId}/download-session/chunks/{chunkIndex}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> uploadPart(
            @PathVariable UUID fileId,
            @PathVariable Long chunkIndex,
            @RequestHeader("X-Checksum-Value") String checksumValue,
            @RequestHeader("X-Checksum-Algorithm") Checksum.Algorithm checksumAlgorithm,
            HttpServletRequest request) throws IOException {

        uploadChunkUseCase.execute(
                new UploadChunkInput(
                        fileId,
                        checksumValue,
                        checksumAlgorithm,
                        request.getInputStream(),
                        chunkIndex));

        return ResponseEntity.ok().build();

    }

    @PostMapping(path = "download-session")
    public ResponseEntity<Object> createUploadSession(@RequestBody CreateUploadSessionInput input) {
        return ResponseEntity.ok(createUploadSessionUseCase.execute(input));
    }

    @Transactional
    @PostMapping("download-session/abort")
    public ResponseEntity<Void> abort(@RequestBody AbortUploadSessionInput input) {

        abortUploadSessionUseCase.execute(input);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "{fileId}/download-session/complete")
    public ResponseEntity<?> completeSession(@PathVariable UUID fileId) throws IOException {

        completeUploadSessionUseCase.execute(new CompleteUploadSessionInput(fileId));

        return ResponseEntity.ok().build();

    }

}
