package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

import com.github.ruben_bottu.class_scheduler_backend.domain.course_group.CourseGroup;

public record CourseGroupDTO(
        Long id,
        CourseDTO course,
        GroupDTO group
) {

    public CourseGroupDTO(CourseGroup courseGroup) {
        this(courseGroup.id(), new CourseDTO(courseGroup.course()), new GroupDTO(courseGroup.group()));
    }
}
