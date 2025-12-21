package com.arqivame.storage.infrastructure.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.apache.commons.io.input.ThrottledInputStream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.arqivame.storage.application.file.session.chunk.write.WriteUploadSessionChunkInput;
import com.arqivame.storage.application.file.session.chunk.write.WriteUploadSessionChunkUseCase;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionInput;
import com.arqivame.storage.application.file.session.create.CreateUploadSessionUseCase;
import com.arqivame.storage.domain.file.Checksum;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    // @PostMapping()

    private final CreateUploadSessionUseCase createUploadSessionUseCase;
    private final WriteUploadSessionChunkUseCase writeUploadSessionChunkUseCase;

    public TestController(CreateUploadSessionUseCase createUploadSessionUseCase,
            WriteUploadSessionChunkUseCase writeUploadSessionChunkUseCase) {
        this.createUploadSessionUseCase = createUploadSessionUseCase;
        this.writeUploadSessionChunkUseCase = writeUploadSessionChunkUseCase;
    }

    @PutMapping(value = "{fileId}/sessions/{sessionId}/chunks/{chunkPart}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> uploadPart(
            @PathVariable UUID fileId,
            @PathVariable UUID sessionId,
            @PathVariable Long chunkPart,
            HttpServletRequest request) { // Usamos o request para pegar o stream puro

        // 1. Validação da Chave ANTES de ler o corpo
        // Claims claims = valetKeyService.parse(valetKey);
        // Se falhar aqui, o arquivo de 1TB nem começou a ser transmitido de fato

        // 2. Pegar o stream original do socket
        try (InputStream rawStream = request.getInputStream()) {

            writeUploadSessionChunkUseCase
                    .execute(new WriteUploadSessionChunkInput(
                            fileId,
                            sessionId,
                            "123-abc",
                            Checksum.Algorithm.MD5,
                            rawStream,
                            chunkPart));

            // 3. Aplicar o limite de velocidade lido do Token
            // // int rateLimit = claims.get("rate", Integer.class);
            // ThrottledInputStream throttledStream = ThrottledInputStream.builder()
            // .setInputStream(rawStream)
            // .setMaxBytes(10000 * 1024, ChronoUnit.SECONDS)
            // .get();

            // // ThrottledInputStream throttledStream = new ThrottledInputStream(rawStream,
            // // 100 * 1024); // Exemplo: 100 KB/s

            // // 4. Gravar diretamente no Storage
            // // storageService.savePart(id, n, throttledStream);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping(path = "{fileId}/sessions")
    public ResponseEntity<Object> createUploadSession(final UUID fileId) {

        final var input = new CreateUploadSessionInput(
                fileId,
                2_742_190_080L,
                Duration.ofHours(1),
                1,
                1024L,
                "123-abc",
                Checksum.Algorithm.MD5);

        return ResponseEntity.ok(createUploadSessionUseCase.execute(input));

    }

}
