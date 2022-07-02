package com.blusalt.dele.consumer_service.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Auditable<U> {

    protected U createdBy;

    protected Date createdDate;

    protected U modifiedBy;

    protected Date modifiedDate;

    protected U lastModifiedBy;

    protected Date lastModifiedDate;

}
