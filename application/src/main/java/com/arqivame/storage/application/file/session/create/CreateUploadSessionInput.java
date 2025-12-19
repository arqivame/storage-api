package com.arqivame.storage.application.file.session.create;

import java.time.Duration;
import java.util.UUID;

public record CreateUploadSessionInput(UUID fileId, Integer totalChunks, Duration idleTimeout) {

}
