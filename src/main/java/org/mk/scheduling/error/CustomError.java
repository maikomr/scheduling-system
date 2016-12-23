package org.mk.scheduling.error;

import lombok.Data;

/**
 * Created by maiko on 23/12/2016.
 */
@Data
public class CustomError {

    private String property;
    private String message;

    public CustomError(String property, String message) {
        this.property = property;
        this.message = message;
    }

}
