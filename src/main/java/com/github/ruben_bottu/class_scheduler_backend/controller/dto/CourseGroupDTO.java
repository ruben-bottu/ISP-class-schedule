package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

public record CourseGroupDTO(
        Long id,
        CourseDTO course,
        GroupDTO group
) {
}
