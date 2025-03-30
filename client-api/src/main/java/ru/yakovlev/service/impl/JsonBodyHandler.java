package ru.yakovlev.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<T> {
    private final Class<T> type;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonBodyHandler(Class<T> type) {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.type = type;
    }

    @Override
    public HttpResponse.BodySubscriber<T> apply(HttpResponse.ResponseInfo responseInfo) {
        return HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> {
                    try {
                        return objectMapper.readValue(body, type);
                    } catch (Exception e) {
                        log.warn("Failed to parse {}. Reason: {}", body, e.getMessage());
                        return null;
                    }
                }
        );
    }
}
