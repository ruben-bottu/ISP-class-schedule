package com.github.ruben_bottu.class_scheduler_backend.domain.course;

import java.util.List;

public interface CourseRepository {
    List<Course> getAll();

    int countByIdIn(List<Long> courseIds);
}
