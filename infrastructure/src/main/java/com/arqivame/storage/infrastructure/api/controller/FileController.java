package com.arqivame.storage.infrastructure.api.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/testThrotle")
    public ResponseEntity<Void> throtle(
            final @RequestParam("file") MultipartFile chunk,
            final @RequestParam("fileId") UUID fileId,
            final @RequestParam("sessionId") UUID sessionId,
            final @RequestParam("chunkIndex") Integer chunkIndex) throws IOException {

        // ThrottledInputStream throttledInputStream = new ThrottledInputStream(
        //         chunk.getInputStream(),
        //         100 * 1024 // Limite de 100 KB/s
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

        // final UploadSessionChunkInput.Chunk chunkInput = new UploadSessionChunkInput.Chunk(
        //         chunk.getInputStream(),
        //         chunkIndex);

        // final var aham = new UploadSessionChunkInput(fileId, sessionId, chunkInput);

        // uploadSessionChunkUseCase.execute(aham);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // @PutMapping("/uploads/{uploadId}/parts/{partNumber}")
    // public ResponseEntity<PartResult> uploadPart(
    //         @PathVariable String uploadId,
    //         @PathVariable int partNumber,
    //         @RequestHeader("X-Part-Checksum") String clientChecksum,
    //         InputStream inputStream // Spring injeta o InputStream do request
    // ) {
    //     // 1. Envolve o InputStream para calcular o hash enquanto ele é lido
    //     try (
    //             DigestInputStream dis = new DigestInputStream(inputStream, MessageDigest.getInstance("SHA-256"));
    //             FileOutputStream fos = new FileOutputStream(getChunkFile(uploadId, partNumber)) // Stream para o disco
    //     ) {
    //         // 2. Transferir (IOUtils.copy) os bytes do DIS para o FOS
    //         long bytesWritten = IOUtils.copyLarge(dis, fos);

    //         // 3. Checar Integridade
    //         String serverChecksum = Hex.encodeHexString(dis.getMessageDigest().digest());
    //         if (!serverChecksum.equals(clientChecksum)) {
    //             // Logar e lançar exceção. O cliente deve tentar novamente.
    //             return ResponseEntity.badRequest().build();
    //         }

    //         // 4. Salvar metadados no DB/Redis para garantir o estado
    //         uploadService.markPartCompleted(uploadId, partNumber, serverChecksum);

    //         return ResponseEntity.ok().build();
    //     } catch (IOException | NoSuchAlgorithmException e) {
    //         // Tratar erros de I/O ou Hash
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //     }
    // }

}
