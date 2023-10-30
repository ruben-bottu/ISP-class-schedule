package com.github.ruben_bottu.class_scheduler_backend.domain.course_group;

import java.util.List;

public interface CourseGroupRepository {

    List<List<CourseGroup>> getGroupedByCourseIn(List<Long> courseIds);
}
