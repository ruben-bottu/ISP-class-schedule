package com.github.ruben_bottu.isp_class_schedule_backend.domain.lesson;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassGroup;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;

import java.time.LocalDateTime;

public record Lesson(
        Long id,
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        Course course,
        ClassGroup classGroup
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lessonDTO = (Lesson) o;

        return id.equals(lessonDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
