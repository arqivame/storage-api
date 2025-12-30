package com.arqivame.storage.application.file.session.chunk.merge;

import java.util.UUID;

public record MergeUploadSessionChunksInput(UUID fileId, UUID uploadSessionId) {

}
