package ru.yakovlev.service.impl;

import lombok.SneakyThrows;

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

class SearchEngine {

    private static final String APP_DATAFILE_PATH = "app.datafile.path";

    private final String datafilesPath;

    public SearchEngine(Properties properties) {
        this.datafilesPath = properties.getProperty(APP_DATAFILE_PATH);
    }

    List<String> search(String searchRequest) {
        File datafilesFolder = Path.of(datafilesPath).toFile();
        File[] files = datafilesFolder.listFiles();
        List<String> result = new ArrayList<>();

        if (files == null || files.length == 0) return result;

        return Arrays.stream(files)
                .flatMap(file -> readFile(file).stream())
                .filter(s -> s.contains(searchRequest))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private List<String> readFile(File file) {
        String absolutePath = file.getAbsolutePath();
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        }
        return result;
    }
}
