package ru.yakovlev;

import lombok.SneakyThrows;
import ru.yakovlev.service.MainMenu;
import ru.yakovlev.service.impl.RecommendationServiceImpl;
import ru.yakovlev.service.impl.SearchServiceImpl;

import java.io.InputStream;
import java.util.Properties;

public class ClientApi {

    public static void main(String[] args) {
        Properties properties = readProperties();

        new MainMenu(
                new SearchServiceImpl(properties),
                new RecommendationServiceImpl(properties)
        ).start();
    }

    @SneakyThrows
    public static Properties readProperties() {
        Properties properties = new Properties();
        InputStream resourceAsStream = ClientApi.class.getClassLoader().getResourceAsStream("application.properties");
        properties.load(resourceAsStream);
        return properties;
    }

}
