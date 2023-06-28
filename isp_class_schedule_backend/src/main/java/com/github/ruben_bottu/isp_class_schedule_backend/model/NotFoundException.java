package com.github.ruben_bottu.isp_class_schedule_backend.model;

public class NotFoundException extends RuntimeException {
    private final String resourceName;

    public NotFoundException(String resourceName, String message) {
        super(message);
        this.resourceName = resourceName;
    }

    public NotFoundException(String message) {
        this("", message);
    }

    public String getResourceName() {
        return resourceName;
    }
}
