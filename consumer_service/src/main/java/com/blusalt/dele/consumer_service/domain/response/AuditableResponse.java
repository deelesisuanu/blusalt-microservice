package com.blusalt.dele.consumer_service.domain.response;

import lombok.Data;

@Data
public abstract class AuditableResponse {
    protected String createdBy;
    protected Long createdDate;
    protected String lastModifiedBy;
    protected Long lastModifiedDate;
}
