package com.arqivame.storage.application.file.session.cancel;

import java.util.UUID;

public record CancelUploadSessionUseCaseInput(UUID fileId, UUID uploadSessionId) {

}
