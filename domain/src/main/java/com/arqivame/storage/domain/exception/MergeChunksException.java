package com.arqivame.storage.domain.exception;

public class MergeChunksException extends SilentDomainException {

    private static final String MESSAGE = "An error occurred while merging file chunks";

    private MergeChunksException() {
        super(MESSAGE);
    }

    public static MergeChunksException create() {
        return new MergeChunksException();
    }

}
