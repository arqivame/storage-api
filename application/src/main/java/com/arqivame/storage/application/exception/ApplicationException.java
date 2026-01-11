package com.arqivame.storage.application.exception;

import java.util.ArrayList;
import java.util.List;

public class ApplicationException extends RuntimeException {

    private final List<ApplicationException.Error> errors;

    protected ApplicationException(
            final String message,
            final List<ApplicationException.Error> errors,
            final Throwable cause,
            final boolean verbose) {
        super(message, cause, enableSuppression(verbose), writableStackTrace(verbose));
        this.errors = addCauseToErrors(errors, cause) == null ? List.of() : new ArrayList<>(errors);
    }

    public List<ApplicationException.Error> getErrors() {
        return List.copyOf(errors);
    }

    public String errorsToString() {
        return "[" + errors.stream()
                .map(ApplicationException.Error::message)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No errors") + "]";
    }

    private static boolean enableSuppression(final boolean verbose) {
        return verbose ? false : true;
    }

    private static boolean writableStackTrace(final boolean verbose) {
        return verbose ? true : false;
    }

    private static List<ApplicationException.Error> addCauseToErrors(
            final List<ApplicationException.Error> errors,
            final Throwable cause) {
        if (cause == null)
            return errors;

        final List<ApplicationException.Error> result = new ArrayList<>(errors != null ? errors : List.of());
        result.add(ApplicationException.Error.with(cause));
        return result;
    }

    public record Error(String message) {

        public static ApplicationException.Error with(final String message) {
            return new ApplicationException.Error(message);
        }

        public static ApplicationException.Error with(final Throwable cause) {
            return new ApplicationException.Error(
                    "Exception:[" + cause.getClass().getName() + "] Message:[" + cause.getMessage() + "]");
        }

    }
}
