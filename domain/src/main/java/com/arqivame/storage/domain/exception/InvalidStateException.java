package com.arqivame.storage.domain.exception;

import static java.util.Objects.isNull;

import java.util.List;

import com.arqivame.storage.domain.Entity;

public class InvalidStateException extends SilentDomainException {

    private static final String MESSAGE = "[%s] in invalid state.";

    protected <E extends Entity<?>> InvalidStateException(final Class<E> entityClass, final List<Error> errors) {
        super(
                MESSAGE.formatted(entityClass.getSimpleName()),
                isNull(errors) ? List.of() : List.copyOf(errors));
    }

    public static <E extends Entity<?>> InvalidStateException with(final Class<E> entityClass, final Error error) {
        final List<Error> list = isNull(error) ? List.of() : List.of(error);
        return new InvalidStateException(entityClass, list);
    }

    public static <E extends Entity<?>> InvalidStateException with(
            final Class<E> entityClass,
            final List<Error> errors) {
        return new InvalidStateException(entityClass, errors);
    }

}
