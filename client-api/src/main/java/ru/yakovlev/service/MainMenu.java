package ru.yakovlev.service;

import ru.yakovlev.out.Messages;

import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private final Scanner scanner;
    private final SearchService searchService;
    private final RecommendationService recommendationService;
    private String userName;

    public MainMenu(SearchService searchService, RecommendationService recommendationService) {
        this.scanner = new Scanner(System.in);
        this.searchService = searchService;
        this.recommendationService = recommendationService;
    }

    public void start() {
        System.out.println(Messages.WELCOME);
        System.out.println(Messages.PLEASE_INPUT_NAME);
        userName = scanner.next();
        System.out.println(Messages.welcomeMessage(userName));
        while (true) {
            System.out.println(Messages.MAIN_MENU);
            String choice = scanner.next();
            try {
                int i = Integer.parseInt(choice);
                switch (i) {
                    case 1 -> searchGoods();
                    case 2 -> getRecommendations();
                    case 0 -> {
                        return;
                    }
                    default -> noSuchCommand();
                }
            } catch (NumberFormatException e) {
                noSuchCommand();
            }
        }
    }

    private void noSuchCommand() {
        System.out.println(Messages.WRONG_INPUT);
    }

    private void getRecommendations() {
        List<String> recommendation = recommendationService.recommend(userName);
        System.out.println("We recommend the following: ");
        System.out.println(Messages.SEPARATOR);
        recommendation.forEach(System.out::println);
        System.out.println(Messages.SEPARATOR);
    }

    private void searchGoods() {
        System.out.println(Messages.INPUT_SEARCH_REQUEST);
        String searchRequest = scanner.next();
        List<String> result = searchService.search(userName, searchRequest);
        System.out.println(Messages.FOUND_RESULTS);
        System.out.println(Messages.SEPARATOR);
        result.forEach(System.out::println);
        System.out.println(Messages.SEPARATOR);
    }

}
