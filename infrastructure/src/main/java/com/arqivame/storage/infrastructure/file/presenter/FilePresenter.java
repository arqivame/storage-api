package com.arqivame.storage.infrastructure.file.presenter;

import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionMarkedForDeletionEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionProcessingInitiatedEvent;
import com.arqivame.storage.infrastructure.event.presenter.EventPresenter;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCanceledMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionMarkedForDeletionMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionProcessingInitiatedMessage;

public interface FilePresenter {

    public static FileUploadSessionCanceledMessage present(final FileUploadSessionCanceledEvent event) {
        return new FileUploadSessionCanceledMessage(EventPresenter.present(event), toData(event.getData()));
    }

    public static FileUploadSessionMarkedForDeletionMessage present(
            final FileUploadSessionMarkedForDeletionEvent event) {
        return new FileUploadSessionMarkedForDeletionMessage(EventPresenter.present(event), toData(event.getData()));
    }

    public static FileUploadSessionProcessingInitiatedMessage present(
            final FileUploadSessionProcessingInitiatedEvent event) {
        return new FileUploadSessionProcessingInitiatedMessage(EventPresenter.present(event), toData(event.getData()));
    }

    private static FileUploadSessionCanceledMessage.Data toData(final FileUploadSessionCanceledEvent.Data eventData) {
        return new FileUploadSessionCanceledMessage.Data(
                eventData.fileId(),
                eventData.sessionId(),
                eventData.closedAt());
    }

    private static FileUploadSessionMarkedForDeletionMessage.Data toData(
            final FileUploadSessionMarkedForDeletionEvent.Data eventData) {
        return new FileUploadSessionMarkedForDeletionMessage.Data(
                eventData.fileId(),
                eventData.sessionId());
    }

    private static FileUploadSessionProcessingInitiatedMessage.Data toData(
            final FileUploadSessionProcessingInitiatedEvent.Data eventData) {
        return new FileUploadSessionProcessingInitiatedMessage.Data(
                eventData.fileId(),
                eventData.sessionId());
    }

}
