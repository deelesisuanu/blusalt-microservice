package com.blusalt.dele.consumer_service.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
public class AppResponse<T> {
    private int status;
    private String message;
    private T data;
    @Builder.Default
    private Double execTime = 0D;
    @Builder.Default
    private Object error = new ArrayList<>();
}