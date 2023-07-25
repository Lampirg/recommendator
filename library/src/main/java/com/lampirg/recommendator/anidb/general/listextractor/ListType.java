package com.lampirg.recommendator.anidb.general.listextractor;

public enum ListType {
    COMPLETED("completed"),
    WATCHING("watching"),
    DROPPED("dropped"),
    ON_HOLD("on_hold");

    private final String defaultValue;

    ListType(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
