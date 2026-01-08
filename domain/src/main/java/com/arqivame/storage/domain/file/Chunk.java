package com.arqivame.storage.domain.file;

import java.util.Objects;

import com.arqivame.storage.domain.ValueObject;
import com.arqivame.storage.domain.validation.ValidationError;
import com.arqivame.storage.domain.validation.ValidationHandler;

public record Chunk(
        Long index,
        Long size) implements ValueObject {

    public static Chunk create(final Long index, final Long size) {
        return new Chunk(index, size);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (Objects.isNull(index))
            handler.append(ValidationError.with("Chunk index cannot be null."));
        else if (index < 0)
            handler.append(ValidationError.with("Chunk index must be a non-negative value."));

        if (Objects.isNull(size))
            handler.append(ValidationError.with("Chunk size cannot be null."));
        else if (size < 0)
            handler.append(ValidationError.with("Chunk size must be a non-negative value."));

    }

}
