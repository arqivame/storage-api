package com.arqivame.storage.domain;

import com.arqivame.storage.domain.validation.ValidationHandler;

public interface ValueObject extends Validatable {

    default void validate(ValidationHandler handler) {
    };

}
