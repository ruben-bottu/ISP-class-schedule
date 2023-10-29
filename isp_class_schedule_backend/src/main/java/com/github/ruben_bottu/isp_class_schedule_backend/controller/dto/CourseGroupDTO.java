package com.github.ruben_bottu.isp_class_schedule_backend.controller.dto;

public record CourseGroupDTO(
        Long id,
        CourseDTO course,
        GroupDTO group
) {
}
