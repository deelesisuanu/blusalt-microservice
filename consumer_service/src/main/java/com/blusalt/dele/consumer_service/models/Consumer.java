package com.blusalt.dele.consumer_service.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "consumer")
public class Consumer extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CONSUMER_CODE", nullable = false)
    private String customerCode;

    @Column(name = "CONSUMER_NAME", nullable = false)
    private String customerName;

    @Column(name = "CONSUMER_EMAIL", nullable = false)
    private String customerEmail;

}
