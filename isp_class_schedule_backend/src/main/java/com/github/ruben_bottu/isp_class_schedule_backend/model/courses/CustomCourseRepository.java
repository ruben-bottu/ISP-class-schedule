package com.github.ruben_bottu.isp_class_schedule_backend.model.courses;

import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassGroupDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.Pair;

import java.util.List;

public interface CustomCourseRepository {

    List<Pair<CourseDTO, List<ClassGroupDTO>>> getCoursesWithClassGroups(List<Long> courseIds);
}
