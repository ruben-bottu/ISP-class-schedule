package com.github.ruben_bottu.isp_class_schedule_backend.data_access;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassGroup;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.Pair;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;

import java.util.List;

public interface CustomCourseRepository {

    List<Pair<Course, List<ClassGroup>>> getCoursesWithClassGroups(List<Long> courseIds);
}
