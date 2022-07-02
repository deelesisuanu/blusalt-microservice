package com.blusalt.dele.consumer_service.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public abstract class Auditable<U> {

    protected U createdBy;

    protected Date createdDate;

    protected U modifiedBy;

    protected Date modifiedDate;

    protected U lastModifiedBy;

    protected Date lastModifiedDate;

}
