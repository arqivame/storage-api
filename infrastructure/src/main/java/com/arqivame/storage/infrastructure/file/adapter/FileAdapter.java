package com.arqivame.storage.infrastructure.file.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.arqivame.storage.application.usecase.file.session.upload.create.CreateUploadSessionInput;
import com.arqivame.storage.domain.file.Checksum;

public interface FileAdapter {

    public static CreateUploadSessionInput adaptCreateUploadSessionInput(JwtAuthenticationToken authentication) {

        Jwt jwt = authentication.getToken();

        UUID fileId = Optional.<UUID>ofNullable(jwt.getClaim("fileId")).orElse(null);

        return null;
    }

}
