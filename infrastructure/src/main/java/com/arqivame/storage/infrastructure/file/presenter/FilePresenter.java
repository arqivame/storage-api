package com.arqivame.storage.infrastructure.file.presenter;

import com.arqivame.storage.domain.file.event.FileUploadSessionCanceledEvent;
import com.arqivame.storage.infrastructure.event.presenter.EventPresenter;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCanceledMessage;

public interface FilePresenter {

    public static FileUploadSessionCanceledMessage present(final FileUploadSessionCanceledEvent event) {
        return (FileUploadSessionCanceledMessage) EventPresenter.present(event, FilePresenter::toData);
    }

    private static FileUploadSessionCanceledMessage.Data toData(final FileUploadSessionCanceledEvent.Data eventData) {
        return new FileUploadSessionCanceledMessage.Data(
                eventData.fileId(),
                eventData.sessionId(),
                eventData.closedAt());
    }

}
