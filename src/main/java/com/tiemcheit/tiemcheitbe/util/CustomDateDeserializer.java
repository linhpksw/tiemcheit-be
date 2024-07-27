package com.tiemcheit.tiemcheitbe.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CustomDateDeserializer extends StdDeserializer<Date> {

    private static final List<String> dateFormats = Arrays.asList(
            "yyyy-MM-dd HH:mm:ss", // Format with time
            "yyyy-MM-dd"           // Format without time
    );

    public CustomDateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        if (date != null) {
            for (String format : dateFormats) {
                try {
                    return new SimpleDateFormat(format).parse(date);
                } catch (ParseException e) {
                    // Try the next format
                }
            }
        }
        throw new JsonParseException(p, "Unparseable date: \"" + date +
                "\". Supported formats: " + String.join(", ", dateFormats));
    }
}
