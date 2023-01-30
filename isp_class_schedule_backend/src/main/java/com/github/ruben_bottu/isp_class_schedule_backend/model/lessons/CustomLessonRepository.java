package com.github.ruben_bottu.isp_class_schedule_backend.model.lessons;

import com.github.ruben_bottu.isp_class_schedule_backend.model.CourseAndClassGroupDTO;

import java.util.List;

public interface CustomLessonRepository {

    int countOverlaps(List<CourseAndClassGroupDTO> list);
    int countOverlapsBetween(CourseAndClassGroupDTO element, List<CourseAndClassGroupDTO> list);
}
