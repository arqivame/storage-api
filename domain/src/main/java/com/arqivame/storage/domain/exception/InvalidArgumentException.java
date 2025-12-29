package com.arqivame.storage.domain.exception;

import static java.util.Objects.isNull;

import java.util.List;

public class InvalidArgumentException extends SilentDomainException {

    private static final String MESSAGE = "Invalid argument provided.";

    protected InvalidArgumentException(final List<Error> errors) {
        super(MESSAGE, isNull(errors) ? List.of() : List.copyOf(errors));
    }

    public static InvalidArgumentException with(final Error error) {
        final List<Error> list = isNull(error) ? List.of() : List.of(error);
        return new InvalidArgumentException(list);
    }

    public static InvalidArgumentException with(final List<Error> errors) {
        return new InvalidArgumentException(errors);
    }

}
