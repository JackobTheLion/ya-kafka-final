package ru.yakovlev;

import lombok.SneakyThrows;
import org.apache.spark.sql.SparkSession;
import ru.yakovlev.service.AnalyticsService;
import ru.yakovlev.service.AnalyticsServiceMock;
import ru.yakovlev.util.SparkUtils;

import java.io.InputStream;
import java.util.Properties;

public class Analytics {

    public static void main(String[] args) {
        SparkSession sparkSession = SparkUtils.getSparkSession();
        AnalyticsService analyticsService = new AnalyticsServiceMock(sparkSession, readProperties());
        analyticsService.start();
    }

    @SneakyThrows
    public static Properties readProperties() {
        Properties properties = new Properties();
        InputStream resourceAsStream = Analytics.class.getClassLoader().getResourceAsStream("applications.properties");
        properties.load(resourceAsStream);
        return properties;
    }
}
