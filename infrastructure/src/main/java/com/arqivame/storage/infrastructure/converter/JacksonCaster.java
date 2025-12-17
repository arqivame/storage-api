package com.arqivame.storage.infrastructure.converter;

import org.springframework.stereotype.Component;

import com.arqivame.storage.infrastructure.configuration.mapper.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JacksonCaster implements Caster {

    private static final ObjectMapper mapper = Mapper.mapper();

    @Override
    public <T> T cast(Object value, Class<T> targetType) {
        return mapper.convertValue(value, targetType);
    }

}
