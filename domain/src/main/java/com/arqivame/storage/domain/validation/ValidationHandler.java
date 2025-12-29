package com.arqivame.storage.domain.validation;

import static java.util.Objects.requireNonNullElse;

import java.util.List;

import com.arqivame.storage.domain.exception.DomainException;

public interface ValidationHandler {

    ValidationHandler append(final ValidationError error);

    ValidationHandler append(final ValidationHandler handler);

    <T> T validate(final Validation<T> validation);

    void validate(final ValidationVoid validation);

    List<ValidationError> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default List<DomainException.Error> getDomainErrors() {
        return requireNonNullElse(
                getErrors()
                        .stream()
                        .map(ValidationError::toDomainError)
                        .toList(),
                List.of());
    }

    @FunctionalInterface
    public interface Validation<T> {

        T validate();

    }

    @FunctionalInterface
    public interface ValidationVoid {

        void validate();

    }

}
