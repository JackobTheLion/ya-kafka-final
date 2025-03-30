package ru.yakovlev.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import ru.yakovlev.dto.Product;
import ru.yakovlev.dto.BlacklistRecord;
import ru.yakovlev.serdes.StopWordSerde;

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class BlacklistFilter {

    public static final String BLACKLIST_GOODS_TOPIC_NAME = "app.blacklist.topic";
    protected final StreamsBuilder builder;
    protected final Properties properties;
    private final Set<String> blacklist;

    BlacklistFilter(StreamsBuilder builder, Properties properties) {
        this.builder = builder;
        this.properties = properties;
        this.blacklist = Collections.synchronizedSet(new HashSet<>());
    }

    KStream<String, Product> applyFilter(KStream<String, Product> goods) {
        buildUpdateBlacklistStream(properties.getProperty(BLACKLIST_GOODS_TOPIC_NAME));

        return goods.filter((key, message) -> {
            for (String string : blacklist) {
                if (message.getName().contains(string)) {
                    log.info("Goods {} did not pass blacklist", message);
                    return false;
                }
            }
            return true;
        });
    }

    private void buildUpdateBlacklistStream(String topic) {
        builder.stream(topic, Consumed.with(Serdes.Void(), new StopWordSerde()))
                .foreach((key, blacklistRecord) -> updateStopWords(blacklistRecord));
    }

    private void updateStopWords(BlacklistRecord blacklistRecord) {
        if (blacklistRecord == null) return;
        String word = blacklistRecord.goodsName();
        if (blacklistRecord.add()) {
            log.info("Adding goodsName '{}' to stop list", word);
            blacklist.add(word);
        } else {
            log.info("Removing goodsName '{}' from stop list", word);
            blacklist.remove(word);
        }
    }
}
