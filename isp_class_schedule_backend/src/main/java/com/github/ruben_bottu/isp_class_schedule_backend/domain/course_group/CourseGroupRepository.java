package com.github.ruben_bottu.isp_class_schedule_backend.domain.course_group;

import java.util.List;

public interface CourseGroupRepository {

    List<List<CourseGroup>> getGroupedByCourseIn(List<Long> courseIds);
}
