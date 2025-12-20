package com.arqivame.storage.infrastructure.file.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import com.arqivame.storage.infrastructure.file.model.ChunkMetadata;

public interface FileAdapter {

//     static UploadSessionChunkInput adapt(final MultipartFile chunk, final Jwt jwt) {

//         return null;
//         // try {
//         //     final ChunkMetadata chunkMetadata = adapt(jwt);

//         //     // final UploadSessionChunkInput.Chunk chunkInput = new UploadSessionChunkInput.Chunk(
//         //     //         chunk.getInputStream(),
//         //     //         chunkMetadata.index());

//         //     return new UploadSessionChunkInput(chunkMetadata.fileId(), chunkMetadata.sessionId(), chunkInput);
//         // } catch (IOException e) {
//         //     throw InternalErrorException.with("An Error ocurred on adapt MultipartFile to UploadSessionChunkInput", e);
//         // }
//     }

    private static ChunkMetadata adapt(final Jwt jwt) {

        final var throwable = new RuntimeException();

        final UUID fileId = Optional.<UUID>ofNullable(jwt.getClaim("fileId"))
                .orElseThrow(() -> throwable);
        final UUID sessionId = Optional.<UUID>ofNullable(jwt.getClaim("sessionId"))
                .orElseThrow(() -> throwable);
        final Integer chunkIndex = Optional.<Integer>ofNullable(jwt.getClaim("chunkIndex"))
                .orElseThrow(() -> throwable);

        return new ChunkMetadata(
                fileId,
                sessionId,
                chunkIndex);
    }

}
