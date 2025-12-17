package com.arqivame.storage.domain.validation.handler;

import java.util.ArrayList;
import java.util.List;

import com.arqivame.storage.domain.exception.DomainException;
import com.arqivame.storage.domain.validation.ValidationError;
import com.arqivame.storage.domain.validation.ValidationHandler;

public class Notification implements ValidationHandler {

    private final List<ValidationError> errors;

    private Notification(final List<ValidationError> errors) {
        this.errors = errors == null ? new ArrayList<>() : new ArrayList<>(errors);
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final ValidationError error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public Notification append(final ValidationError error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(final Validation<T> validation) {
        try {
            return validation.validate();
        } catch (DomainException e) {
            this.errors.addAll(e.getErrors().stream().map(ValidationError::fromDomainError).toList());
        } catch (Throwable e) {
            this.errors.add(new ValidationError(e.getMessage()));
        }

        return null;
    }

    @Override
    public void validate(final ValidationVoid validation) {
        try {
            validation.validate();
        } catch (DomainException e) {
            this.errors.addAll(e.getErrors().stream().map(ValidationError::fromDomainError).toList());
        } catch (Throwable e) {
            this.errors.add(new ValidationError(e.getMessage()));
        }
    }

    @Override
    public List<ValidationError> getErrors() {
        return this.errors == null ? List.of() : List.copyOf(this.errors);
    }

}
