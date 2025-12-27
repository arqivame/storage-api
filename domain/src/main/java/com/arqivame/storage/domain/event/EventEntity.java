package com.arqivame.storage.domain.event;

import com.arqivame.storage.domain.Entity;

public record EventEntity(String type, String id) {

    public static EventEntity of(final Entity<?> entity) {
        return new EventEntity(entity.getClass().getSimpleName(), entity.getId().getStringValue());
    }

}
