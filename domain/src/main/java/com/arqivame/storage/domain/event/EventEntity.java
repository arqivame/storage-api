package com.arqivame.storage.domain.event;

import com.arqivame.storage.domain.Entity;
import com.arqivame.storage.domain.Identifier;

public record EventEntity(String type, String id) {

    public static <E extends Entity<I>, I extends Identifier<?>> EventEntity of(final E entity) {
        return new EventEntity(entity.getClass().getSimpleName(), entity.getId().getStringValue());
    }

}
