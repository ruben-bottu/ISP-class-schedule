package com.github.ruben_bottu.isp_class_schedule_backend.model;

import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseDTO;

public record CourseAndClassGroupDTO(
        CourseDTO course,
        ClassGroupDTO classGroup
) {}
