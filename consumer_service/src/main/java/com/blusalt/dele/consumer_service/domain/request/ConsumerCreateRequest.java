package com.blusalt.dele.consumer_service.domain.request;

import com.blusalt.dele.consumer_service.validator.IsUnique;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerCreateRequest {

    @NotNull(message = "Consumer Name is Required")
    private String consumerName;

    @IsUnique(tableName = "consumer", columnName = "consumerCode", message = "Consumer Code has to be unique")
    private String consumerCode;

    @IsUnique(tableName = "consumer", columnName = "consumerEmail", message = "Consumer Email has to be unique")
    private String consumerEmail;
}
