package com.arqivame.storage.application.exception;

import java.util.List;

public abstract class SilentApplicationException extends ApplicationException {

    private static final boolean VERBOSE = false;

    protected SilentApplicationException(final String message) {
        super(message, null, null, VERBOSE);
    }

    protected SilentApplicationException(
            final String message,
            final List<ApplicationException.Error> errors) {
        super(message, errors, null, VERBOSE);
    }

    protected SilentApplicationException(
            final String message,
            final List<ApplicationException.Error> errors,
            final Throwable cause) {
        super(message, errors, cause, VERBOSE);
    }

}
