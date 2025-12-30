package com.arqivame.storage.infrastructure.file.model;

import java.io.Serializable;
import java.util.UUID;

import com.arqivame.storage.infrastructure.event.model.EventMessage;

public record FileUploadSessionProcessingInitiatedMessage(
        EventMessage.Metadata metadata,
        Data data)
        implements EventMessage<FileUploadSessionProcessingInitiatedMessage.Data> {

    public record Data(
            UUID fileId,
            UUID sessionId) implements Serializable {
    }

}
