package com.arqivame.storage.application.file.session.delete.physical;

import java.util.UUID;

public record PhysicalUploadSessionDeleteInput(UUID fileId, UUID uploadSessionId) {

}
