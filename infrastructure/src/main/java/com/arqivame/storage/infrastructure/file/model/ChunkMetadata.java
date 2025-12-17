package com.arqivame.storage.infrastructure.file.model;

import java.util.UUID;

public record ChunkMetadata(
        UUID fileId,
        UUID sessionId,
        Integer index) {

}
