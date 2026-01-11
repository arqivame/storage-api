package com.arqivame.storage.application.exception;

import java.util.List;
import java.util.Objects;

public class MaxConcurrentChunkWritesReachedException extends SilentApplicationException {

    private static final String MESSAGE = "Maximum concurrent chunk writes reached [%d]";
    private static final String ERROR = MESSAGE
            + ", please wait for some chunk writes to complete before uploading more chunks";

    private MaxConcurrentChunkWritesReachedException(final Integer maxConcurrentWrites) {
        super(
                MESSAGE.formatted(Objects.requireNonNull(maxConcurrentWrites)),
                List.of(ApplicationException.Error.with(ERROR.formatted(Objects.requireNonNull(maxConcurrentWrites)))));
    }

    public static MaxConcurrentChunkWritesReachedException create(final Integer maxConcurrentWrites) {
        return new MaxConcurrentChunkWritesReachedException(maxConcurrentWrites);
    }

}
