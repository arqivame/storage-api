package com.arqivame.storage.domain.event;

import java.io.Serializable;

public abstract class EventHandler<D extends Serializable> {

    private final String eventKey;

    protected EventHandler(String eventKey) {
        this.eventKey = eventKey;
    }

    public String eventKey() {
        return eventKey;
    }

    public abstract void handle(Event<D> event);

}
