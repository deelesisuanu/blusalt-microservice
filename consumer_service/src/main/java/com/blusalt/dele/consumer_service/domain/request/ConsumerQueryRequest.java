package com.blusalt.dele.consumer_service.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerQueryRequest {
    private Long consumerId;
    private String consumerName;

    @JsonIgnore
    @Builder.Default
    private Integer page = 1;

    @JsonIgnore
    @Builder.Default
    private Integer size = 25;
}
