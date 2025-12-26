package com.arqivame.storage.infrastructure.event.presenter;

import java.io.Serializable;
import java.util.function.Function;

import com.arqivame.storage.domain.event.Event;
import com.arqivame.storage.domain.event.EventEntity;
import com.arqivame.storage.infrastructure.event.model.EventMessage;

public interface EventPresenter {

    public static <D extends Serializable, T extends Serializable> EventMessage<D> present(
            final Event<T> event,
            final Function<T, D> dataMapper) {
        return new EventMessage<>(
                event.getDomain(),
                event.getEntity(),
                event.getAction(),
                event.getService(),
                event.getVersion(),
                event.getOccurredAt(),
                event.getRelatedEntities()
                        .stream()
                        .map(EventPresenter::present)
                        .collect(java.util.stream.Collectors.toSet()),
                dataMapper.apply(event.getData()));
    }

    private static EventMessage.EventEntity present(final EventEntity eventEntity) {
        return new EventMessage.EventEntity(eventEntity.type(), eventEntity.id());
    }

}
