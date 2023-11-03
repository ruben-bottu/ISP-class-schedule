package com.github.ruben_bottu.class_scheduler_backend.controller.dto;

import com.github.ruben_bottu.class_scheduler_backend.domain.course.Course;

public record CourseDTO(
        Long id,
        String name
) {

    public CourseDTO(Course course) {
        this(course.id(), course.name());
    }
}
