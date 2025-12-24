package com.arqivame.storage.domain.exception;

import java.util.List;

public class VerboseDomainException extends DomainException {

    private static final boolean VERBOSE = true;

    protected VerboseDomainException(
            final String message,
            final List<DomainException.Error> errors,
            final Throwable cause) {
        super(message, errors, cause, VERBOSE);
    }

}
