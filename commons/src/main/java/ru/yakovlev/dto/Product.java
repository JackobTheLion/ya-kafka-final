package ru.yakovlev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Product {
    @JsonProperty("product_id")
    private String productId;

    private String name;

    private String description;

    private Price price;

    private String category;

    private String brand;

    private Stock stock;

    private String sku;

    private List<String> tags;

    private List<Image> images;

    private Map<String, String> specifications;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;

    private String index;

    @JsonProperty("store_id")
    private String storeId;
}
