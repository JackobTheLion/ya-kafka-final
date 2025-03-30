package ru.yakovlev.service.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class RowData {
    @JsonProperty("columns")
    private List<List<String>> columns;
}
