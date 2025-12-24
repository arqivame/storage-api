package com.arqivame.storage.application.file.session.delete;

import java.util.UUID;

public record DeleteUploadSessionUseCaseInput(UUID fileId, UUID uploadSessionId) {

}
