package com.github.ruben_bottu.isp_class_schedule_backend.domain.class_;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseGroup;

import java.time.LocalDateTime;

public record ClassDTO(
        Long id,
        LocalDateTime startTimestamp,
        LocalDateTime endTimestamp,
        CourseGroup courseGroup
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassDTO classDTO = (ClassDTO) o;

        return id.equals(classDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
