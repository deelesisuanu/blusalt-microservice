package com.blusalt.dele.consumer_service.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Constants {

    public static final String APP_CONTEXT = "api/v1/";

    public interface ApiResponseMessage {
        String SUCCESSFUL = "Successfully processed";
        String PENDING = "Pending approval";
        String FAILED = "Failed request";
        String UPDATE = "Successfully updated";
        String GET = "Successfully fetched records";
    }
}
