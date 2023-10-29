package com.github.ruben_bottu.isp_class_schedule_backend.controller.dto;

import java.time.LocalDateTime;

public record ClassDTO(
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        String courseName,
        String groupName
) {
}
