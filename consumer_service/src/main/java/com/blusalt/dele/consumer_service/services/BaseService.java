package com.blusalt.dele.consumer_service.services;

import com.blusalt.dele.consumer_service.config.BaseConfig;
import com.blusalt.dele.consumer_service.domain.request.ConsumerCreateRequest;
import com.blusalt.dele.consumer_service.domain.request.ConsumerQueryRequest;
import com.blusalt.dele.consumer_service.domain.request.PaginationRequest;
import com.blusalt.dele.consumer_service.domain.response.ConsumerResponse;
import com.blusalt.dele.consumer_service.domain.response.PagedResponse;
import com.blusalt.dele.consumer_service.models.Consumer;
import com.blusalt.dele.consumer_service.repository.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.common.collections.Maps;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BaseService {

    private final BaseRepository baseRepository;
//    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    private final BaseConfig baseConfig;

    public ConsumerResponse createConsumer(ConsumerCreateRequest consumerCreateRequest) {
        Consumer consumer = baseRepository.save(baseConfig.modelMapper().map(consumerCreateRequest, Consumer.class));
        return baseConfig.modelMapper().map(consumer, ConsumerResponse.class);
    }

    public PagedResponse<ConsumerResponse> queryConsumer(ConsumerQueryRequest consumerQueryRequest) {
        Map<String, Object> filter = objectMapper.convertValue(consumerQueryRequest, Map.class);
        if (filter.containsKey("consumerId")) {
            filter.put("id", filter.get("consumerId"));
            filter.remove("consumerId");
        }
        filter.computeIfPresent("consumerName", (k, v) -> Maps.of("consumerName", ("%" + v + "%")));

        Page<Consumer> consumers = baseRepository.findAllBy(Consumer.class, filter, PaginationRequest.builder().page(consumerQueryRequest.getPage()).size(consumerQueryRequest.getSize()).build());
        return baseConfig.modelMapper().map(consumers, new TypeToken<PagedResponse<ConsumerResponse>>(){}.getType());
    }

}
