package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

import java.time.LocalDateTime;

public record ClassDTO(
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        String courseName,
        String groupName
) {
}
