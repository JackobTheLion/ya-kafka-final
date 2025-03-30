package ru.yakovlev.service;

import java.util.List;

public interface RecommendationService {

    List<String> recommend(String userName);

}
