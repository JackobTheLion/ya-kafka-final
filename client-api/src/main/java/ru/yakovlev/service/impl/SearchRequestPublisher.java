package ru.yakovlev.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class SearchRequestPublisher {

    private static final String BOOTSTRAP_SERVER = "app.stats.bootstrap.server";
    private static final String STATS_TOPIC = "app.stats.topic";

    private final Properties properties;
    private final String topic;
    private final ObjectMapper objectMapper;

    public SearchRequestPublisher(Properties properties) {
        this.properties = buildProducerProperties(properties);
        this.topic = properties.getProperty(STATS_TOPIC);
        this.objectMapper = new ObjectMapper();
    }

    @SneakyThrows
    void publish(String userName, String searchRequest) {
        StatsRecord statsRecord = new StatsRecord(userName, searchRequest);
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, objectMapper.writeValueAsString(statsRecord));
            producer.send(record);
        }
    }

    private Properties buildProducerProperties(Properties props) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, props.getProperty(BOOTSTRAP_SERVER));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }

    record StatsRecord(String name, String request) {
    }

}
