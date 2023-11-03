package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

import com.github.ruben_bottu.class_scheduler_backend.domain.class_.ClassSummary;

import java.time.LocalDateTime;

public record ClassDTO(
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        String courseName,
        String groupName
) {

    public ClassDTO(ClassSummary summary) {
        this(summary.startTimestamp(), summary.endTimestamp(), summary.courseName(), summary.groupName());
    }
}
