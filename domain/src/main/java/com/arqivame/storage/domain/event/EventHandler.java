package com.arqivame.storage.domain.event;

import java.io.Serializable;

public abstract class EventHandler<E extends Event<? extends Serializable>> {

    private final String eventKey;

    protected EventHandler(final String eventKey) {
        this.eventKey = eventKey;
    }

    public String eventKey() {
        return eventKey;
    }

    public abstract void handle(E event);

}
