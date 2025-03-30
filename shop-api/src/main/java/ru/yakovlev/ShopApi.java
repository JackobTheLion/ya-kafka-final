package ru.yakovlev;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yakovlev.dto.Product;

import java.util.List;
import java.util.Properties;

@Slf4j
public class ShopApi {

    public static void main(String[] args) {
        List<Product> products = new ProductsReader().readProductsFromFile();
        Properties properties = readProperties();
        new GoodsSender(properties).sendProductsToKafka(products);
    }

    @SneakyThrows
    private static Properties readProperties() {
        Properties properties = new Properties();
        properties.load(ShopApi.class.getClassLoader().getResourceAsStream("application.properties"));
        return properties;
    }
}