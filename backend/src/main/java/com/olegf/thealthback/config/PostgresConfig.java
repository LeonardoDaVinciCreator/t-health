package com.olegf.thealthback.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olegf.thealthback.domain.entity.Nutrition;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PostgresConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(
                List.of(
                        new ParametersToJsonbConverter(objectMapper),
                        new JsonbToParametersConverter(objectMapper)
                )
        );
    }

    @WritingConverter
    public static class ParametersToJsonbConverter implements Converter<Nutrition.Parameters, PGobject> {

        private final ObjectMapper mapper;

        public ParametersToJsonbConverter(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public PGobject convert(Nutrition.Parameters source) {
            try {
                PGobject pgObject = new PGobject();
                pgObject.setType("jsonb");
                pgObject.setValue(mapper.writeValueAsString(source));
                return pgObject;
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert Parameters to PGobject jsonb", e);
            }
        }
    }

    @ReadingConverter
    public static class JsonbToParametersConverter implements Converter<PGobject, Nutrition.Parameters> {

        private final ObjectMapper mapper;

        public JsonbToParametersConverter(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        @Override
        public Nutrition.Parameters convert(PGobject source) {
            try {
                if (source == null || source.getValue() == null) return null;
                return mapper.readValue(source.getValue(), Nutrition.Parameters.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert PGobject jsonb to Parameters", e);
            }
        }
    }

}