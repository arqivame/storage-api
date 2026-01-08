package com.arqivame.storage.infrastructure.file.presenter;

import com.arqivame.storage.domain.file.event.FileUploadSessionAbortedEvent;
import com.arqivame.storage.domain.file.event.FileUploadSessionCompletedEvent;
import com.arqivame.storage.infrastructure.event.presenter.EventPresenter;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionAbortedMessage;
import com.arqivame.storage.infrastructure.file.model.FileUploadSessionCompletedMessage;

public interface FilePresenter {

    public static FileUploadSessionCompletedMessage present(final FileUploadSessionCompletedEvent event) {
        return new FileUploadSessionCompletedMessage(
                EventPresenter.present(event),
                new FileUploadSessionCompletedMessage.Data(event.getData().fileId()));
    }

    public static FileUploadSessionAbortedMessage present(final FileUploadSessionAbortedEvent event) {
        return new FileUploadSessionAbortedMessage(
                EventPresenter.present(event),
                new FileUploadSessionAbortedMessage.Data(event.getData().fileId()));
    }

}
