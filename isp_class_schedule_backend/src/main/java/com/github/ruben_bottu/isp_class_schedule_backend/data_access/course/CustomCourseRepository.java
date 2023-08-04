package com.github.ruben_bottu.isp_class_schedule_backend.data_access.course;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseGroup;

import java.util.List;

public interface CustomCourseRepository {

    // TODO move to CourseGroupRepository
    List<List<CourseGroup>> getCourseGroupsGroupedByCourse(List<Long> courseIds);
}
