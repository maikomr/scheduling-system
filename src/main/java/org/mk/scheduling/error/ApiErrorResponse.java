package org.mk.scheduling.error;

import lombok.Data;

/**
 * Created by maiko on 23/12/2016.
 */
@Data
public class ApiErrorResponse {

    private int status;
    private int code;
    private String message;

    public ApiErrorResponse(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
