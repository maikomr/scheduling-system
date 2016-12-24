package org.mk.scheduling.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;


/**
 * Created by maiko on 24/12/2016.
 */
public class JSR310DateTimeSerializer extends JsonSerializer<TemporalAccessor> {

    private static final DateTimeFormatter ISOFormatter =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    @Override
    public void serialize(TemporalAccessor value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        JavaTimeModule module = new JavaTimeModule();
        JSR310DateTimeSerializer INSTANCE = new JSR310DateTimeSerializer();
        module.addSerializer(OffsetDateTime.class, INSTANCE);
        module.addSerializer(ZonedDateTime.class, INSTANCE);
        module.addSerializer(LocalDateTime.class, INSTANCE);
        module.addSerializer(Instant.class, INSTANCE);

        generator.writeString(ISOFormatter.format(value));
    }
}
