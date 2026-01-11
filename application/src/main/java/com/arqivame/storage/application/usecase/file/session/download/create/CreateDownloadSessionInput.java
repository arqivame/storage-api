package com.arqivame.storage.application.usecase.file.session.download.create;

import java.util.UUID;

public record CreateDownloadSessionInput(
        UUID fileId,
        Integer maxChunksAtSameTime,
        Long maxBytesPerSecondTransferRatePerChunk) {

}
