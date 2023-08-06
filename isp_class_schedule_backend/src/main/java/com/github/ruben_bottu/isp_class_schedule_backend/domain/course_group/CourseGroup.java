package com.github.ruben_bottu.isp_class_schedule_backend.domain.course_group;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.Group;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;

public record CourseGroup(
        Long id,
        Course course,
        Group group
) {
}
