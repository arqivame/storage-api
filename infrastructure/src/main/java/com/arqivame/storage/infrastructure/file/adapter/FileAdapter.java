package com.arqivame.storage.infrastructure.file.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.arqivame.storage.application.file.session.create.CreateUploadSessionInput;
import com.arqivame.storage.domain.file.Checksum;

public interface FileAdapter {

    public static CreateUploadSessionInput adaptCreateUploadSessionInput(JwtAuthenticationToken authentication) {

        Jwt jwt = authentication.getToken();

        UUID fileId = Optional.<UUID>ofNullable(jwt.getClaim("fileId")).orElse(null);

        return new CreateUploadSessionInput(
                fileId,
                1024L,
                300L,
                2,
                10240L,
                "123-abc",
                Checksum.Algorithm.MD5);
    }

}
