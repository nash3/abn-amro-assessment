package com.nhira.abnrecipeapp.utils.enums;

public enum ResponseCode {
    SUCCESS("Operation completed successfully"),
    ERROR("Operation failed");

    private final String description;

    ResponseCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
