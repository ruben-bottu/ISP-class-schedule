package com.github.ruben_bottu.class_scheduler_backend.domain;

public class NotFoundException extends RuntimeException {
    private final String resourceName;

    public NotFoundException(String resourceName, String message) {
        super(message);
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
