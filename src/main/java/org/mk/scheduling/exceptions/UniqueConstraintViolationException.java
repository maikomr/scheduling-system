package org.mk.scheduling.exceptions;

/**
 * Created by maiko on 23/12/2016.
 */
public class UniqueConstraintViolationException extends Exception {

    private String property;

    public UniqueConstraintViolationException(String property) {
        super("already exist");
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
