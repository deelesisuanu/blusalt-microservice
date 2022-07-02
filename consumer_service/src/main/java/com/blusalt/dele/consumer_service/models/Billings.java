package com.blusalt.dele.consumer_service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "billings")
public class Billings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TRANSACTION_REFERENCE", nullable = false)
    private String transactionReference;

    @Column(name = "CONSUMER_ID", nullable = false)
    private Long consumerId;

    @Column(name = "AMOUNT_BILLED", nullable = false, precision = 19, scale = 2)
    private BigDecimal amountBilled;

}
