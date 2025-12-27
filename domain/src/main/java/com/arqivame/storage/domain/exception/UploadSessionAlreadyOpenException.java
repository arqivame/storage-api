package com.arqivame.storage.domain.exception;

import java.util.List;

public class UploadSessionAlreadyOpenException extends SilentDomainException {

    private static final String MESSAGE = "Session already open";
    private static final String ERROR = MESSAGE + ", please close the current session before opening a new one";

    private UploadSessionAlreadyOpenException() {
        super(MESSAGE, List.of(Error.with(ERROR)));
    }

    public static UploadSessionAlreadyOpenException create() {
        return new UploadSessionAlreadyOpenException();
    }

}
