package ru.yakovlev.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yakovlev.util.MapperUtil;

@Slf4j
public class GeneralDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper mapper = MapperUtil.getMapper();

    private final TypeReference<T> typeReference;

    public GeneralDeserializer(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) return null;
        try {
            return mapper.readValue(data, typeReference);
        } catch (Exception e) {
            log.error("Deserialization of type {} from topic {} failed: {}.", typeReference, topic, e.getMessage());
            return null;
        }
    }
}
