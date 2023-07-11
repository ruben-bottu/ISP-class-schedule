package com.github.ruben_bottu.isp_class_schedule_backend.domain.course;

public record Course(
        Long id,
        String name
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course courseDTO = (Course) o;

        return id.equals(courseDTO.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
