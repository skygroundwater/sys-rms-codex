package com.colvir.ms.sys.rms.manual.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ContextObjectMapper extends ObjectMapper {
    private static ContextObjectMapper INSTANCE;

    private ContextObjectMapper() {
        //место для конфигурации маппера
        this.registerModule(new JavaTimeModule());
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static ContextObjectMapper get() {
        if (INSTANCE == null) {
            INSTANCE = new ContextObjectMapper();
        }
        return INSTANCE;
    }
}
