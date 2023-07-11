package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.CourseEntity;

public class CourseBuilder {

    public static CourseEntity with(Long id, String name) {
        var course = new CourseEntity();
        course.setId(id);
        course.setName(name);
        return course;
    }
}
