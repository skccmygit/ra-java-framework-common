package kr.co.skcc.oss.com.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public final class Java8DateTimeModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    private static String DEFAULT_DATE_FORMAT;
    private static String DEFAULT_TIME_FORMAT;
    private static String DEFAULT_DATETIME_FORMAT;

    public String getDefaultDateFormat() {
        return DEFAULT_DATE_FORMAT;
    }

    public String getDefaultTimeFormat() {
        return DEFAULT_TIME_FORMAT;
    }

    public String getDefaultDateTimeFormat() {
        return DEFAULT_DATETIME_FORMAT;
    }

    @Value("${spring.mvc.format.date}")
    public void setDefaultDateFormat(String format) {
        DEFAULT_DATE_FORMAT = format;
    }

    @Value("${spring.mvc.format.time}")
    public void setDefaultTimeFormat(String format) {
        DEFAULT_TIME_FORMAT = format;
    }

    @Value("${spring.mvc.format.date-time}")
    public void setDefaultDateTimeFormat(String format) {
        DEFAULT_DATETIME_FORMAT = format;
    }

    public Java8DateTimeModule() {
        super(PackageVersion.VERSION);

        addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(
                    LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT).format(localDate));
            }
        });

        addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override
            public void serialize(
                    LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT).format(localTime));
            }
        });

        addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(
                    LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT).format(localDateTime));
            }
        });

        addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return LocalDate.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            }
        });

        addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return LocalTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            }
        });

        addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return LocalDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
            }
        });
    }
}