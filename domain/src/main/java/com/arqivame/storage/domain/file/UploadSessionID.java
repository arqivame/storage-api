package com.arqivame.storage.domain.file;

import java.util.UUID;

import com.arqivame.storage.domain.Identifier;

public class UploadSessionID extends Identifier<UUID> {

    private UploadSessionID(final UUID id) {
        super(id);
    }

    public static UploadSessionID of(final UUID id) {
        return new UploadSessionID(id);
    }

    public static UploadSessionID unique() {
        return UploadSessionID.of(UUID.randomUUID());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    @Override
    public String toString() {
        return "UploadSessionID [value=" + getValue() + "]";
    }

}
