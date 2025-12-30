package com.arqivame.storage.domain.exception;

public class UploadSessionAlreadyProcessingException extends SilentDomainException {

    private static final String MESSAGE = "Upload session is already being processed";

    private UploadSessionAlreadyProcessingException() {
        super(MESSAGE);
    }

    public static UploadSessionAlreadyProcessingException create() {
        return new UploadSessionAlreadyProcessingException();
    }

}
