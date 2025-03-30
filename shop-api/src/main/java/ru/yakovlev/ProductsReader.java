package ru.yakovlev;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.yakovlev.dto.Product;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsReader {

    private static final String DATA_JSON = "data.json";

    private final ObjectMapper mapper;

    public ProductsReader() {
        this.mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
    }

    @SneakyThrows
    public List<Product> readProductsFromFile() {
        try (BufferedReader br =
                     new BufferedReader(
                             new InputStreamReader(Objects.requireNonNull(ProductsReader.class.getClassLoader().getResourceAsStream(DATA_JSON))))) {
            List<Product> result = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                result.add(mapper.readValue(line, Product.class));
            }
            return result;
        }
    }
}
