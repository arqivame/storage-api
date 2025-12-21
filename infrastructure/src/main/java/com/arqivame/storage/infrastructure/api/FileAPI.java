package com.arqivame.storage.infrastructure.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arqivame.storage.domain.file.Checksum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Files")
@RequestMapping("files")
public interface FileAPI {

    @Operation(summary = "Upload a chunk of a file to an existing upload session", description = "This method uploads a chunk of a file to an existing upload session", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "session/chunk", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<Void> uploadChunk(
            HttpServletRequest request,
            JwtAuthenticationToken authentication,
            @RequestHeader(value = "Checksum-Value", required = false) String checksumValue,
            @RequestHeader(value = "Checksum-Algorithm", required = false) Checksum.Algorithm checksumAlgorithm);

    @Operation(summary = "Create a new upload session for a file", description = "This method creates a new upload session for a file", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("sessions")
    ResponseEntity<Object> createUploadSession(JwtAuthenticationToken authentication);

}
