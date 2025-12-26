package com.arqivame.storage.infrastructure.event.presenter;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventEntity;
import com.arqivame.storage.infrastructure.event.model.EventMessage;

public interface EventPresenter {

    public static EventMessage.Metadata present(final Event<?> event) {
        return new EventMessage.Metadata(
                event.getDomain(),
                event.getEntity(),
                event.getAction(),
                event.getService(),
                event.getVersion(),
                event.getOccurredAt(),
                event.getRelatedEntities()
                        .stream()
                        .map(EventPresenter::present)
                        .collect(java.util.stream.Collectors.toSet()));
    }

    private static EventMessage.EventEntity present(final EventEntity eventEntity) {
        return new EventMessage.EventEntity(eventEntity.type(), eventEntity.id());
    }

}
