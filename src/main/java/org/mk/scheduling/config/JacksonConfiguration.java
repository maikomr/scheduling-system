package org.mk.scheduling.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by maiko on 24/12/2016.
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        JavaTimeModule module = new JavaTimeModule();
        JSR310DateTimeSerializer INSTANCE = new JSR310DateTimeSerializer();
        module.addSerializer(OffsetDateTime.class, INSTANCE);
        module.addSerializer(ZonedDateTime.class, INSTANCE);
        module.addSerializer(LocalDateTime.class, INSTANCE);
        module.addSerializer(Instant.class, INSTANCE);

        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findModulesViaServiceLoader(true)
                .modulesToInstall(module);
    }
}
