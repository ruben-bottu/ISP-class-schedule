package com.github.ruben_bottu.class_scheduler_backend.domain.class_;

import java.time.LocalDateTime;

public record ClassSummary(
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        String courseName,
        String groupName
) {
}
