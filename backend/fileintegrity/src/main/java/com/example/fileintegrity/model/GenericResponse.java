package com.example.fileintegrity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@RequiredArgsConstructor
@Data
public class GenericResponse<T> {
    private int statusCode;
    private T data;
    private String message;

    public GenericResponse(int statusCode, T data, String message) {
        this.statusCode = statusCode;
        this.data = data;
        this.message = message;
    }
}
