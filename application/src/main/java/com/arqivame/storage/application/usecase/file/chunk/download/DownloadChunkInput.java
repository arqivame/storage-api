package com.arqivame.storage.application.usecase.file.chunk.download;

import java.util.UUID;

public record DownloadChunkInput(
        UUID fileId,
        Long chunkIndex) {

}
