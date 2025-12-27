package com.arqivame.storage.domain.event;

import java.util.Objects;

public abstract class Event<D> {

    private final EventMetadata metadata;
    private final D data;

    protected Event(final EventMetadata metadata, final D data) {
        this.metadata = Objects.requireNonNull(metadata);
        this.data = data;
    }

    public String key() {
        return metadata.domain() + "." + metadata.entity() + "." + metadata.action();
    }

    public EventMetadata getMetadata() {
        return metadata;
    }

    public D getData() {
        return data;
    }

}
