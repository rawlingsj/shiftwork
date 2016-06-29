package com.teammachine.staffrostering.domain.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JSR310LocalDateSerializer extends JsonSerializer<LocalDate>  {

    public static final DateTimeFormatter ISOFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final JSR310LocalDateSerializer INSTANCE = new JSR310LocalDateSerializer();

    private JSR310LocalDateSerializer() {
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(ISOFormatter.format(localDate));
    }
}
