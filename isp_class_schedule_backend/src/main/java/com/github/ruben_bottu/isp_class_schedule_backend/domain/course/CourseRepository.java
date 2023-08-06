package com.github.ruben_bottu.isp_class_schedule_backend.domain.course;

import java.util.List;

public interface CourseRepository {
    List<Course> getAll();

    int countByIdIn(List<Long> courseIds);
}
