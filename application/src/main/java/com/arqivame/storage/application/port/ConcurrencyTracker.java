package com.arqivame.storage.application.port;

import com.arqivame.storage.domain.Identifier;

public interface ConcurrencyTracker {

    void increment(Identifier<?> key, String... tags);

    void decrement(Identifier<?> key, String... tags);

    Integer getCurrentCount(Identifier<?> key, String... tags);

}
