package com.arqivame.storage.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final ValidationError error);

    ValidationHandler append(final ValidationHandler handler);

    <T> T validate(final Validation<T> validation);

    void validate(final ValidationVoid validation);

    List<ValidationError> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
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
