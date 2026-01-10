package com.arqivame.storage.domain.exception;

import java.util.List;
import java.util.Objects;

public class MaxConcurrentChunkReadsReachedException extends SilentDomainException {

    private static final String MESSAGE = "Maximum concurrent chunk reads reached [%d]";
    private static final String ERROR = MESSAGE
            + ", please wait for some chunk reads to complete before downloading more chunks";

    private MaxConcurrentChunkReadsReachedException(final Integer maxConcurrentReads) {
        super(
                MESSAGE.formatted(Objects.requireNonNull(maxConcurrentReads)),
                List.of(Error.with(ERROR.formatted(Objects.requireNonNull(maxConcurrentReads)))));
    }

    public static MaxConcurrentChunkReadsReachedException create(final Integer maxConcurrentReads) {
        return new MaxConcurrentChunkReadsReachedException(maxConcurrentReads);
    }

}
