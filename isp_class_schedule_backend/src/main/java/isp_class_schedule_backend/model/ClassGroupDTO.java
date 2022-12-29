package isp_class_schedule_backend.model;

public record ClassGroupDTO(
        Long id,
        String name
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassGroupDTO that = (ClassGroupDTO) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
