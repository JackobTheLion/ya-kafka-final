package ru.yakovlev.service.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KsqlDbResponse {
    @JsonProperty("row")
    private RowData row;
}
