package ru.yakovlev.util;

import org.apache.spark.sql.SparkSession;

public class SparkUtils {

    private SparkUtils(){
    }

    public static SparkSession getSparkSession() {
        return SparkSession.builder()
                .appName("AnalyticsService")
                .master("local[*]")
                .getOrCreate();
    }
}
