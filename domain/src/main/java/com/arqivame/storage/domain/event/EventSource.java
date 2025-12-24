package com.arqivame.storage.domain.event;

import java.util.Optional;

@FunctionalInterface
public interface EventSource {

    Optional<Event<?>> nextEvent();

}
