package com.arqivame.storage.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import com.arqivame.storage.domain.file.Checksum;
import com.arqivame.storage.infrastructure.api.FileAPI;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class FileController implements FileAPI {

    @Override
    public ResponseEntity<Object> createUploadSession(JwtAuthenticationToken authentication) {

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Override
    public ResponseEntity<Void> uploadChunk(
            HttpServletRequest request,
            JwtAuthenticationToken authentication,
            String checksumValue,
            Checksum.Algorithm checksumAlgorithm) {

        return ResponseEntity.status(HttpStatus.OK).build();

    }

}
