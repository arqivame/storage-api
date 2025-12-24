package com.arqivame.storage.infrastructure.api.controller;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.arqivame.storage.domain.exception.DomainException;
import com.arqivame.storage.domain.exception.InternalErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ApiError> handle(final Throwable ex) {
        return ResponseEntity.internalServerError().body(ApiError.with("Internal Server Error ->" + ex.getMessage()));
    }

    @ExceptionHandler(value = InternalErrorException.class)
    public ResponseEntity<ApiError> handle(final InternalErrorException ex) {
        return ResponseEntity.unprocessableContent().body(ApiError.with("Internal Server Error"));
    }

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ApiError> handle(final DomainException ex) {
        return ResponseEntity.unprocessableContent().body(ApiError.from(ex));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Void> handle(final NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiError> handle(final InvalidDataAccessApiUsageException ex) {
        return ResponseEntity.badRequest()
                .body(ApiError.with("Invalid Data Access Api Usage [%s]".formatted(ex.getMessage())));
    }

}
