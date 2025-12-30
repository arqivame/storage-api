package com.arqivame.storage.infrastructure.commons;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class SequentialIterator<T> implements Iterator<T> {

    private final ConcurrentHashMap<Long, T> items;
    private AtomicLong actualPosition;

    public static <T> SequentialIterator<T> of(final Set<Item<T>> items) {
        return new SequentialIterator<>(items);
    }

    private SequentialIterator(final Set<Item<T>> items) {

        validate(items);

        this.actualPosition = new AtomicLong(0L);
        this.items = new ConcurrentHashMap<>(
                items
                        .stream()
                        .collect(Collectors
                                .toMap(
                                        item -> item.position,
                                        item -> item.value)));

    }

    private static <T> void validate(final Set<Item<T>> items) {

        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("Items cannot be null or empty.");

        final List<Long> positions = items.stream()
                .map(Item::position)
                .sorted()
                .toList();

        for (int i = 1; i < positions.size(); i++) {
            final long expected = positions.get(i - 1) + 1;
            if (positions.get(i) != expected) {
                throw new IllegalArgumentException(
                        "Invalid sequence. Expected position "
                                + expected
                                + " but found "
                                + positions.get(i));
            }
        }
    }

    public boolean hasNext() {
        return items.containsKey(actualPosition.get());
    }

    public T next() {
        final Long position = actualPosition.getAndIncrement();
        final T item = items.get(position);
        if (Objects.isNull(item))
            throw new IllegalStateException("No more items available at position: " + position);
        return item;
    }

    public static record Item<T>(T value, Long position) {

        public static <T> Item<T> of(final T value, final Long position) {
            return new Item<>(value, position);
        }

    }

}