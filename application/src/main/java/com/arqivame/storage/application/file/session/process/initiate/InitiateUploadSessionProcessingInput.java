package com.arqivame.storage.application.file.session.process.initiate;

import java.util.UUID;

public record InitiateUploadSessionProcessingInput(UUID fileId, UUID uploadSessionId) {

}
