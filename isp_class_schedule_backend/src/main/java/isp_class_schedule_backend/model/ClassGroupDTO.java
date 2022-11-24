package isp_class_schedule_backend.model;

import lombok.Getter;

@Getter
public class ClassGroupDTO {

    private final int id;

    private final String name;

    public ClassGroupDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassGroupDTO that = (ClassGroupDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
