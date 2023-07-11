package com.github.ruben_bottu.isp_class_schedule_backend.data_access;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseAndClassGroup;

import java.util.List;

public interface CustomLessonRepository {

    int countOverlaps(List<CourseAndClassGroup> list);
    int countOverlapsBetween(CourseAndClassGroup element, List<CourseAndClassGroup> list);
}
