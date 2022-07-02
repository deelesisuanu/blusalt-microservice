package com.blusalt.dele.consumer_service.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Consumer extends Auditable<String> {
    private String customerId;
    private String customerName;
    private String customerEmail;
}
