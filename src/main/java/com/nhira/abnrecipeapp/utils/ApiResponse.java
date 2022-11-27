package com.nhira.abnrecipeapp.utils;

import com.nhira.abnrecipeapp.utils.enums.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T body;
    private String narrative;
    private boolean successful;
    private ResponseCode responseCode;

    public <U> ApiResponse<U> map(Function<T, U> mapper) {
        return ApiResponse.<U>builder()
                .body(mapper.apply(body))
                .narrative(this.narrative)
                .successful(this.successful)
                .responseCode(this.responseCode)
                .build();
    }
}
