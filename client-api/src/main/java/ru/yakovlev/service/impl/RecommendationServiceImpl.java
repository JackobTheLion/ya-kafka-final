package ru.yakovlev.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.yakovlev.service.RecommendationService;
import ru.yakovlev.service.recommendation.KsqlDbResponse;
import ru.yakovlev.service.recommendation.RowData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationServiceImpl implements RecommendationService {

    private final static String RECOMMENDATION_TABLE = "app.recommendation.table";
    private final static String DB_ENDPOINT = "app.db.endpoint";

    private final HttpClient httpClient;
    private final String endpoint;
    private final String queryTemplate;
    private final ObjectMapper objectMapper;

    public RecommendationServiceImpl(Properties properties) {
        this.httpClient = HttpClient.newHttpClient();
        String recommendationTable = properties.getProperty(RECOMMENDATION_TABLE);
        this.queryTemplate = "SELECT RECOMMENDED_PRODUCTS FROM " + recommendationTable + " WHERE username = '%s';";
        this.endpoint = properties.getProperty(DB_ENDPOINT);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @SneakyThrows
    public List<String> recommend(String userName) {
        String ksqlQuery = String.format(queryTemplate, userName);
        Map<String, Object> requestBody = Map.of(
                "ksql", ksqlQuery,
                "streamsProperties", Map.of()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/vnd.ksql.v1+json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                .build();

        HttpResponse<KsqlDbResponse[]> response = httpClient.send(request, new JsonBodyHandler<>(KsqlDbResponse[].class));
        if (response == null) return new ArrayList<>();
        else return Arrays.stream(response.body())
                .map(KsqlDbResponse::getRow)
                .filter(Objects::nonNull)
                .map(RowData::getColumns)
                .filter(Objects::nonNull)
                .flatMap(lists -> lists.stream().flatMap(List::stream))
                .collect(Collectors.toList());
    }

}
