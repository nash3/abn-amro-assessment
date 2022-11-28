package com.nhira.abnrecipeapp.utils;

import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import lombok.Generated;

@Generated
public interface Utils {
    static <T> ApiResponse<T> createResponse(
            T body, boolean successful, ResponseCode responseCode, String narrative) {
        return ApiResponse.<T>builder()
                .body(body)
                .successful(successful)
                .responseCode(responseCode)
                .narrative(narrative)
                .build();
    }

    static <T> ApiResponse<T> createResponse(
            T body, boolean successful, ResponseCode responseCode) {
        return ApiResponse.<T>builder()
                .body(body)
                .successful(successful)
                .responseCode(responseCode)
                .narrative(responseCode.getDescription())
                .build();
    }
}
