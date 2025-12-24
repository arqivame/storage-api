package com.arqivame.storage.infrastructure.api.controller;

import java.util.List;

import com.arqivame.storage.domain.exception.DomainException;

public record ApiError(String message, List<String> errors) {

    static ApiError with(final String message) {
        return new ApiError(message, List.of());
    }

    static ApiError from(final DomainException ex) {
        return new ApiError(ex.getMessage(), ex.getErrors().stream().map(DomainException.Error::message).toList());
    }
}