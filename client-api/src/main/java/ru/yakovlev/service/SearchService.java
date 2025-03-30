package ru.yakovlev.service;

import java.util.List;

public interface SearchService {

    List<String> search(String userName, String searchRequest);

}
