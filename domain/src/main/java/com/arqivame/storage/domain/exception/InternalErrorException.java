package com.arqivame.storage.domain.exception;

import java.util.List;

public class InternalErrorException extends VerboseDomainException {

    protected InternalErrorException(
            final String message,
            final List<DomainException.Error> errors,
            final Throwable cause) {
        super(message, errors, cause);
    }

    public static InternalErrorException with(final Throwable cause) {
        return new InternalErrorException(cause.getMessage(), List.of(), cause);
    }

    public static InternalErrorException with(final String message, final Throwable cause) {
        return new InternalErrorException(message, List.of(), cause);
    }

    public static InternalErrorException with(
            final String message,
            final List<DomainException.Error> errors,
            final Throwable cause) {
        return new InternalErrorException(message, errors, cause);
    }

}
