package com.nhira.abnrecipeapp.utils.enums;

public enum ResponseCode {
    SUCCESS("Operation completed successfully"),

    ERROR("Operation failed"),

    NOT_FOUND("Not found");

    private final String description;

    ResponseCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
