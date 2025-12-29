package com.arqivame.storage.domain.exception;

import java.util.List;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.Identifier;

public class NotFoundException extends SilentDomainException {

    private static final String MESSAGE_TEMPLATE = "[%S] not found";
    private static final String ERROR_TEMPLATE = "[%s] with id [%s] not found";

    private <E extends Entity<I>, I extends Identifier<?>> NotFoundException(
            final Class<E> entityClass,
            final I identifier) {
        super(
                MESSAGE_TEMPLATE.formatted(entityClass.getSimpleName()),
                List.of(Error.with(
                        ERROR_TEMPLATE.formatted(
                                entityClass.getSimpleName(),
                                identifier.getStringValue()))));
    }

    private <E extends Entity<I>, I extends Identifier<?>> NotFoundException(final Class<E> entityClass) {
        super(
                MESSAGE_TEMPLATE.formatted(entityClass.getSimpleName()),
                List.of(Error.with(MESSAGE_TEMPLATE.formatted(entityClass.getSimpleName()))));
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity<I>, I extends Identifier<?>> NotFoundException create(final Entity<I> entity) {
        return new NotFoundException(entity.getClass(), entity.getId());
    }

    public static <E extends Entity<I>, I extends Identifier<?>> NotFoundException create(
            final Class<E> entityClass,
            final I identifier) {
        return new NotFoundException(entityClass, identifier);
    }

    public static <E extends Entity<I>, I extends Identifier<?>> NotFoundException create(final Class<E> entityClass) {
        return new NotFoundException(entityClass);
    }

}
