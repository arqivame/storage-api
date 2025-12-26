package com.arqivame.storage.infrastructure.configuration.messaging;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class MessageConverterConfig {

    private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
            .dateFormat(new StdDateFormat())
            .featuresToDisable(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, // TODO verify if its necessary
                    DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                    DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .modules(new JavaTimeModule())
            .build();

    @Bean
    public MessageConverter customMessageConverter() {

        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        return converter;

        // MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        // // ObjectMapper mapper = new ObjectMapper();

        // // // 1. Suporte a Instant / Java Time
        // // mapper.registerModule(new JavaTimeModule());
        // // mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // // // 2. Suporte a Records e parâmetros no construtor (Essencial para seu 'Data')
        // // mapper.registerModule(new com.fasterxml.jackson.module.paramnames.ParameterNamesModule());

        // // 3. (Opcional) Inclusão de tipo para resolver o genérico <D>
        // // Isso adiciona uma propriedade "@class" no JSON para o Jackson saber o tipo
        // // exato de D
        // // mapper.activateDefaultTyping(
        // // mapper.getPolymorphicTypeValidator(),
        // // ObjectMapper.DefaultTyping.NON_FINAL
        // // );

        // converter.setObjectMapper(mapper);
        // return converter;
    }

}
