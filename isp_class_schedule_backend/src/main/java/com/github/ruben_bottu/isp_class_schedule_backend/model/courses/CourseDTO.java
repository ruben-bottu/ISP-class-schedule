package com.github.ruben_bottu.isp_class_schedule_backend.model.courses;

public record CourseDTO(
        Long id,
        String name
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseDTO courseDTO = (CourseDTO) o;

        return id.equals(courseDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
