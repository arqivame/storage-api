package com.arqivame.storage.application.file.session.delete.mark;

import java.util.UUID;

public record MarkUploadSessionForDeletionInput(UUID fileId, UUID uploadSessionId) {

}
