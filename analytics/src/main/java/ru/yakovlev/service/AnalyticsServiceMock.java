package ru.yakovlev.service;

import lombok.SneakyThrows;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AnalyticsServiceMock implements AnalyticsService {

    private static final String HDFS_URI = "app.hdfs.uri";
    private static final String BOOTSTRAP_SERVERS = "app.bootstrap.servers";
    private static final String TOPIC = "app.topic";

    private final SparkSession sparkSession;
    private final String hdfsUri;
    private final String bootstraps;
    private final String topic;

    public AnalyticsServiceMock(SparkSession sparkSession, Properties properties) {
        this.sparkSession = sparkSession;
        this.hdfsUri = properties.getProperty(HDFS_URI);
        this.bootstraps = properties.getProperty(BOOTSTRAP_SERVERS);
        this.topic = properties.getProperty(TOPIC);

        System.out.println(properties);

    }

    @Override
    @SneakyThrows
    public void start() {
        sparkSession.readStream()
                .schema(getSchema())
                .json(hdfsUri)
                .map((MapFunction<Row, String>) value -> "{\"username\":\"" + value.getString(0) + "\", \"product_name\":\"phone\"}", Encoders.STRING())
                .writeStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", bootstraps)
                .option("topic", topic)
                .option("checkpointLocation", "./checkpoint")
                .start()
                .awaitTermination();


//        sparkSession.readStream()
//                .format("text")
//                .option("path", hdfsUri)
//                .load()
//                .map((MapFunction<Row, String>) value -> {
//                    System.out.println(value);
//                    return "{\"username\":\"egor\", \"product_name\":\"phone\"}";
//                }, Encoders.STRING())
//                .writeStream()
//                .format("kafka")
//                .option("kafka.bootstrap.servers", bootstraps)
//                .option("topic", topic)
//                .outputMode("append")
//                .start()
//                .awaitTermination();
    }

    private Encoder<Row> getEncoder() {
        return null;
    }

    private StructType getSchema() {
        return new StructType()
                .add(new StructField("name", DataTypes.StringType, false, Metadata.empty()))
                .add(new StructField("request", DataTypes.StringType, false, Metadata.empty()));
    }

    private Dataset<String> getRecommendations() {
        List<String> strings = new ArrayList<>();
        strings.add("{\"foo\":\"1\",\"bar\":\"123\"}");
        return sparkSession.createDataset(strings, Encoders.STRING());
    }
}
