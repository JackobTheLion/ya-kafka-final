package ru.yakovlev;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yakovlev.dto.Product;

import java.util.List;
import java.util.Properties;

public class GoodsSender {

    private static final String BOOTSTRAP_SERVER = "app.bootstrap.server";
    private static final String TOPIC = "app.topic";
    private static final String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";

    private final ObjectMapper mapper;
    private final String topic;
    private final Properties producerProperties;

    public GoodsSender(Properties appProperties) {
        this.mapper = new ObjectMapper();
        this.topic = appProperties.getProperty(TOPIC);
        this.producerProperties = getProducerProperties(appProperties);
    }

    public void sendProductsToKafka(List<Product> products) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties)) {
            for (Product product : products) {
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, product.getName(), mapper.writeValueAsString(product));
                producer.send(record);
                System.out.println("Goods sent " + product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send to Kafka: " + e.getMessage());
        }
    }

    private Properties getProducerProperties(Properties appProperties) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getProperty(BOOTSTRAP_SERVER));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, STRING_SERIALIZER);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, STRING_SERIALIZER);
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        return properties;
    }
}
