package com.arqivame.storage.infrastructure.configuration.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.arqivame.storage.application.port.ConcurrencyTracker;
import com.arqivame.storage.infrastructure.concurrency.redis.RedisConcurrencyTracker;

@Configuration
@ConditionalOnProperty(name = "application.vendor.concurrency-tracker", havingValue = "redis")
public class RedisConfig {

    @Bean
    RedisTemplate<String, Integer> redisTemplate(final RedisConnectionFactory factory) {
        final RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        return template;
    }

    @Bean
    RedisScript<Integer> decrementPositiveScript() {

        final String script = """
                local current = redis.call('get', KEYS[1])
                if not current or tonumber(current) <= 0 then
                    return 0
                else
                    return redis.call('decr', KEYS[1])
                end
                """;

        return new DefaultRedisScript<>(script, Integer.class);
    }

    @Bean
    ConcurrencyTracker redisConcurrencyTracker(
            final RedisTemplate<String, Integer> redisTemplate,
            final RedisScript<Integer> decrementPositiveScript,
            final @Value("${redis.concurrency.tracker.ttl-seconds}") Long ttlSeconds) {
        return new RedisConcurrencyTracker(redisTemplate, decrementPositiveScript, Duration.ofSeconds(ttlSeconds));
    }

}
