package com.arqivame.storage.domain.exception;

import java.util.List;

public class ChunkIntegrityViolationException extends SilentDomainException {

    private static final String MESSAGE = "Chunk integrity violation detected.";

    private ChunkIntegrityViolationException() {
        super(MESSAGE, List.of());
    }

    public static ChunkIntegrityViolationException create() {
        return new ChunkIntegrityViolationException();
    }

}
