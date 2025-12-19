package com.arqivame.storage.application.file.session.chunk.write;

import java.io.InputStream;
import java.util.UUID;

public record WriteUploadSessionChunkInput(UUID fileId, UUID uploadSessionId, InputStream chunkData, int chunkNumber) {

}
