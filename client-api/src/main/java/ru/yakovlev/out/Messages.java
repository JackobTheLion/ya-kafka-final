package ru.yakovlev.out;

public class Messages {

    private Messages() {
    }

    public static final String PLEASE_INPUT_NAME = "Please input name: ";
    public static final String SEPARATOR = "====================================================================";
    public static final String FOUND_RESULTS = "Results found: ";
    public static final String INPUT_SEARCH_REQUEST = "Please input search request: ";
    public static final String WRONG_INPUT = "No such command. Please try again";
    public static final String WELCOME = "Welcome to Client Api!";
    public static final String MAIN_MENU = """
            Please choose action number
            1. Search goods
            2. Get recommendations
            0. Exit
            """;
    private static final String WELCOME_MESSAGE_TEMPLATE = "Welcome, %s!";

    public static String welcomeMessage(String name) {
        return String.format(WELCOME_MESSAGE_TEMPLATE, name);
    }
}
