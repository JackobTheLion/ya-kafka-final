package ru.yakovlev.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Serdes;
import ru.yakovlev.dto.Product;

public class ProductSerde extends Serdes.WrapperSerde<Product> {

    public ProductSerde() {
        super(new GeneralSerializer<>(), new GeneralDeserializer<>(new TypeReference<>() {
        }));
    }
}
