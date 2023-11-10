package com.mogun.backend.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ServiceStatus<T> {

    private int status;
    private String message;

    private T data;

    public static ServiceStatus errorStatus(String res) {

        return ServiceStatus.builder()
                .status(200)
                .message(res)
                .build();
    }
}
