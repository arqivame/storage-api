package com.arqivame.storage.infrastructure.file.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.arqivame.storage.infrastructure.event.model.EventMessage;

public record FileUploadSessionCanceledMessage(
        EventMessage.Metadata metadata,
        Data data)
        implements EventMessage<FileUploadSessionCanceledMessage.Data> {

    public record Data(
            UUID fileId,
            UUID sessionId,
            Instant closedAt) implements Serializable {
    }

}
