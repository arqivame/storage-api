package com.arqivame.storage.application.file.session.create;

import java.util.UUID;

public record CreateUploadSessionOutput(UUID fileId, UUID uploadSessionId) {

}
