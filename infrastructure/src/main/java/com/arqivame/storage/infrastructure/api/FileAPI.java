package com.arqivame.storage.infrastructure.api;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Files")
@RequestMapping("files")
public interface FileAPI {

    @Operation(summary = "Upload a chunk of a file to an existing upload session", description = "This method uploads a chunk of a file to an existing upload session", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(path = "session/chunk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> uploadChunk(
            @RequestParam("chunk") MultipartFile chunk,
            JwtAuthenticationToken authentication);

    @PostMapping("{fileId}/sessions")
    ResponseEntity<Object> createUploadSession(@PathVariable("fileId") UUID fileId);

}
