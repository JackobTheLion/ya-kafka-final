package ru.yakovlev.service.impl;

import lombok.SneakyThrows;
import ru.yakovlev.service.SearchService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {

    private final SearchEngine searchEngine;
    private final SearchRequestPublisher searchRequestPublisher;

    public SearchServiceImpl(Properties properties) {
        this.searchEngine = new SearchEngine(properties);
        this.searchRequestPublisher = new SearchRequestPublisher(properties);
    }

    @Override
    public List<String> search(String userName, String searchRequest) {
        searchRequestPublisher.publish(userName, searchRequest);
        return searchEngine.search(searchRequest);
    }

}
