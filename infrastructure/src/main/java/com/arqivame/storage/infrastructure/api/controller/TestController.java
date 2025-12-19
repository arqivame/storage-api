package com.arqivame.storage.infrastructure.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;

import org.apache.commons.io.input.ThrottledInputStream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    @PutMapping(value = "/uploads/{id}/parts/{n}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> uploadPart(
            @PathVariable String id,
            @PathVariable int n,
            @RequestHeader("X-Valet-Key") String valetKey,
            HttpServletRequest request) { // Usamos o request para pegar o stream puro

        // 1. Validação da Chave ANTES de ler o corpo
        // Claims claims = valetKeyService.parse(valetKey);
        // Se falhar aqui, o arquivo de 1TB nem começou a ser transmitido de fato

        // 2. Pegar o stream original do socket
        try (InputStream rawStream = request.getInputStream()) {

            // 3. Aplicar o limite de velocidade lido do Token
            // int rateLimit = claims.get("rate", Integer.class);
            ThrottledInputStream throttledStream = ThrottledInputStream.builder()
                    .setInputStream(rawStream)
                    .setMaxBytes(10000 * 1024, ChronoUnit.SECONDS)
                    .get();

            // ThrottledInputStream throttledStream = new ThrottledInputStream(rawStream,
            // 100 * 1024); // Exemplo: 100 KB/s

            // 4. Gravar diretamente no Storage
            // storageService.savePart(id, n, throttledStream);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

}
