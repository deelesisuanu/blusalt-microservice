package com.blusalt.dele.consumer_service.controller;

import com.blusalt.dele.consumer_service.domain.request.ConsumerCreateRequest;
import com.blusalt.dele.consumer_service.domain.request.ConsumerQueryRequest;
import com.blusalt.dele.consumer_service.domain.response.AppResponse;
import com.blusalt.dele.consumer_service.domain.response.ConsumerResponse;
import com.blusalt.dele.consumer_service.domain.response.PagedResponse;
import com.blusalt.dele.consumer_service.services.BaseService;
import com.blusalt.dele.consumer_service.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@RestController
@RequestMapping(Constants.APP_CONTEXT + "consumer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class BaseController {

    private final BaseService baseService;

    @PostMapping("")
    public ResponseEntity<AppResponse<ConsumerResponse>> createConsumer(@Valid @RequestBody ConsumerCreateRequest createRequest) {
        ConsumerResponse consumerResponse = baseService.createConsumer(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AppResponse.<ConsumerResponse>builder()
                        .message(Constants.ApiResponseMessage.SUCCESSFUL)
                        .status(HttpStatus.CREATED.value())
                        .data(consumerResponse)
                        .build());
    }

    @GetMapping("")
    public ResponseEntity<AppResponse<PagedResponse<ConsumerResponse>>> queryConsumer(@Valid ConsumerQueryRequest consumerQueryRequest) {
        PagedResponse<ConsumerResponse> response = baseService.queryConsumer(consumerQueryRequest);
        return ResponseEntity.ok()
                .body(AppResponse.<PagedResponse<ConsumerResponse>>builder()
                        .message(Constants.ApiResponseMessage.SUCCESSFUL)
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build());
    }

}
