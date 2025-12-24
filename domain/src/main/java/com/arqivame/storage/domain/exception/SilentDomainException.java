package com.arqivame.storage.domain.exception;

import java.util.List;

public abstract class SilentDomainException extends DomainException {

    private static final boolean VERBOSE = false;

    protected SilentDomainException(String message, List<DomainException.Error> errors) {
        super(message, errors, null, VERBOSE);
    }

    protected SilentDomainException(String message, List<DomainException.Error> errors, Throwable cause) {
        super(message, errors, cause, VERBOSE);
    }

}
