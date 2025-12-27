package com.arqivame.storage.domain.exception;

import java.util.List;

public abstract class SilentDomainException extends DomainException {

    private static final boolean VERBOSE = false;

    protected SilentDomainException(final String message) {
        super(message, null, null, VERBOSE);
    }

    protected SilentDomainException(
            final String message,
            final List<DomainException.Error> errors) {
        super(message, errors, null, VERBOSE);
    }

    protected SilentDomainException(
            final String message,
            final List<DomainException.Error> errors,
            final Throwable cause) {
        super(message, errors, cause, VERBOSE);
    }

}
