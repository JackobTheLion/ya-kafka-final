package ru.yakovlev.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;
import ru.yakovlev.util.MapperUtil;

public class GeneralSerializer<T> implements Serializer<T> {

    private final ObjectMapper mapper;

    public GeneralSerializer() {
        this.mapper = MapperUtil.getMapper();
    }

    @Override
    @SneakyThrows
    public byte[] serialize(String topic, T data) {
        if (data == null) return null;
        return mapper.writeValueAsBytes(data);
    }
}
