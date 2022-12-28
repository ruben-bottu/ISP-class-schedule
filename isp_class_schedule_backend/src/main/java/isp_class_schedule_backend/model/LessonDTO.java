package isp_class_schedule_backend.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LessonDTO {
    @EqualsAndHashCode.Include
    Long id;
    LocalDateTime startTimestamp;
    LocalDateTime endTimestamp;
    String courseName;
    String classGroupName;
}
