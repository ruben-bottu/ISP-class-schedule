package com.github.ruben_bottu.isp_class_schedule_backend.data_access;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseGroup;

import java.util.List;

public interface CustomClassRepository {

    int countOverlaps(List<CourseGroup> list);
    int countOverlapsBetween(CourseGroup element, List<CourseGroup> list);
}
