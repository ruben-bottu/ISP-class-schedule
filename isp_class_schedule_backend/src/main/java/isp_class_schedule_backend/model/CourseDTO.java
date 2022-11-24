package isp_class_schedule_backend.model;

import lombok.Getter;

@Getter
public class CourseDTO {

    private final int id;

    private final String name;

    public CourseDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseDTO courseDTO = (CourseDTO) o;

        return id == courseDTO.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
