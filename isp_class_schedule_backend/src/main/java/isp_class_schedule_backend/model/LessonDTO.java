package isp_class_schedule_backend.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LessonDTO {
    private final int id;
    private final LocalDateTime startTimestamp;
    private final LocalDateTime endTimestamp;
    private final String courseName;
    private final String classGroupName;

    public LessonDTO(int id, LocalDateTime startTimestamp, LocalDateTime endTimestamp, String courseName, String classGroupName) {
        this.id = id;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.courseName = courseName;
        this.classGroupName = classGroupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonDTO lessonDTO = (LessonDTO) o;

        return id == lessonDTO.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
