package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;

public record CourseAndClassGroup(
        Course course,
        ClassGroup classGroup
) {}
