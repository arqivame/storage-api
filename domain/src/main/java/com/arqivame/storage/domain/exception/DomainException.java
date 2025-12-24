package com.arqivame.storage.domain.exception;

import java.util.ArrayList;
import java.util.List;

public abstract class DomainException extends RuntimeException {

    private final List<DomainException.Error> errors;

    protected DomainException(
            final String message,
            final List<DomainException.Error> errors,
            final Throwable cause,
            final boolean verbose) {
        super(message, cause, enableSuppression(verbose), writableStackTrace(verbose));
        this.errors = addCauseToErrors(errors, cause) == null ? List.of() : new ArrayList<>(errors);
    }

    public List<DomainException.Error> getErrors() {
        return List.copyOf(errors);
    }

    public String errorsToString() {
        return "[" + errors.stream()
                .map(DomainException.Error::message)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No errors") + "]";
    }

    private static boolean enableSuppression(final boolean verbose) {
        return verbose ? false : true;
    }

    private static boolean writableStackTrace(final boolean verbose) {
        return verbose ? true : false;
    }

    private static List<DomainException.Error> addCauseToErrors(
            final List<DomainException.Error> errors,
            final Throwable cause) {
        if (cause == null)
            return errors;

        final List<DomainException.Error> result = new ArrayList<>(errors != null ? errors : List.of());
        result.add(DomainException.Error.with(cause));
        return result;
    }

    public record Error(String message) {

        public static DomainException.Error with(final String message) {
            return new DomainException.Error(message);
        }

        public static DomainException.Error with(final Throwable cause) {
            return new DomainException.Error(
                    "Exception:[" + cause.getClass().getName() + "] Message:[" + cause.getMessage() + "]");
        }

    }

}