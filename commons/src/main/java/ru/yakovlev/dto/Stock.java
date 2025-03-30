package ru.yakovlev.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Stock {
    private int available;
    private int reserved;
}
