package ru.yakovlev;

import lombok.SneakyThrows;
import ru.yakovlev.filter.FilterService;

import java.io.InputStream;
import java.util.Properties;

public class GoodsFilterApp {

    public static void main(String[] args) {
        new FilterService(readProperties()).start();
    }

    @SneakyThrows
    public static Properties readProperties() {
        Properties properties = new Properties();
        InputStream resourceAsStream = GoodsFilterApp.class.getClassLoader().getResourceAsStream("application.properties");
        properties.load(resourceAsStream);
        return properties;
    }

}
