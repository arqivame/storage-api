package com.arqivame.storage.infrastructure.file.model;

import java.io.Serializable;
import java.util.UUID;

import com.arqivame.storage.infrastructure.event.model.EventMessage;

public record FileUploadSessionCompletedMessage(EventMessage.Metadata metadata, Data data)
        implements EventMessage<FileUploadSessionCompletedMessage.Data> {

    public record Data(UUID fileId) implements Serializable {
    }

}
