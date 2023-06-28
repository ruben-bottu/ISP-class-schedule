package com.github.ruben_bottu.isp_class_schedule_backend.model.lessons;

import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassGroupDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseDTO;

import java.time.LocalDateTime;

public record LessonDTO(
        Long id,
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        CourseDTO course,
        ClassGroupDTO classGroup
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonDTO lessonDTO = (LessonDTO) o;

        return id.equals(lessonDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
