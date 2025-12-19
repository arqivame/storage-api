package com.arqivame.storage.domain.file;

import java.util.UUID;

import com.arqivame.storage.domain.Identifier;

public class ChunkID extends Identifier<UUID> {

    private ChunkID(final UUID id) {
        super(id);
    }

    public static ChunkID of(final UUID id) {
        return new ChunkID(id);
    }

    public static ChunkID unique() {
        return ChunkID.of(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    @Override
    public String toString() {
        return "ChunkID [value=" + getValue() + "]";
    }

}
