package com.example.flightradar.telegram.enums;

import lombok.Getter;

@Getter
public enum Commands {
    HELP("/help"),
    START("/start"),
    SEARCH("/search"),
    FLIGHT("/flight");

    private final String text;
    Commands(String s) {
        this.text = s;
    }
}
