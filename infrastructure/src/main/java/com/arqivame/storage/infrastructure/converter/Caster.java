package com.arqivame.storage.infrastructure.converter;

@FunctionalInterface
public interface Caster {

    <T> T cast(Object value, Class<T> targetType);

}
