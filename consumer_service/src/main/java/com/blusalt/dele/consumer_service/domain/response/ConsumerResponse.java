package com.blusalt.dele.consumer_service.domain.response;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerResponse extends AuditableResponse {
    private Long id;
    private String customerCode;
    private String customerName;
    private String customerEmail;
}
