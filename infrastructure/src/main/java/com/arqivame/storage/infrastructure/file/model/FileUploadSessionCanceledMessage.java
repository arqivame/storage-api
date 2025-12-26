package com.arqivame.storage.infrastructure.file.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.arqivame.storage.infrastructure.event.model.EventMessage;

public class FileUploadSessionCanceledMessage extends EventMessage<FileUploadSessionCanceledMessage.Data> {

    public static record Data(
            UUID fileId,
            UUID sessionId,
            Instant closedAt) implements Serializable {
    }

}
