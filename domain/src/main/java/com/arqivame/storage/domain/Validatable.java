package com.arqivame.storage.domain;

import com.arqivame.storage.domain.validation.ValidationHandler;

@FunctionalInterface
public interface Validatable {

    void validate(ValidationHandler handler);

}
