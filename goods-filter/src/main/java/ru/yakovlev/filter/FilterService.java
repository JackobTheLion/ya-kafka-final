package ru.yakovlev.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import ru.yakovlev.dto.Product;
import ru.yakovlev.serde.ProductSerde;

import java.util.Properties;

@Slf4j
public class FilterService {

    public static final String SOURCE_TOPIC = "app.source.topic";
    public static final String TARGET_TOPIC = "app.target.topic";

    private final Properties properties;
    private final BlacklistFilter filter;
    private final StreamsBuilder builder;

    public FilterService(Properties properties) {
        this.properties = properties;
        this.builder = new StreamsBuilder();
        this.filter = new BlacklistFilter(builder, properties);
    }

    @SuppressWarnings("resource")
    public void start() {
        KStream<String, Product> rawGoodsKStream = getRawGoodsKStream(builder, properties.getProperty(SOURCE_TOPIC));

        KStream<String, Product> filteredGoods = filter.applyFilter(rawGoodsKStream);

        filteredGoods.peek(((key, value) -> log.info(value.toString())))
                .to(properties.getProperty(TARGET_TOPIC), Produced.with(Serdes.String(), new ProductSerde()));

        Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        kafkaStreams.start();
    }

    private KStream<String, Product> getRawGoodsKStream(StreamsBuilder builder, String rawMessageTopic) {
        return builder.stream(rawMessageTopic, Consumed.with(Serdes.String(), new ProductSerde()));
    }
}
