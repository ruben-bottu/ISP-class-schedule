package com.github.ruben_bottu.isp_class_schedule_backend.domain;

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
