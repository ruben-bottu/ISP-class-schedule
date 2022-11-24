package isp_class_schedule_backend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CourseAndClassGroupDTO {

    private final Course course;
    private final ClassGroup classGroup;

    public CourseAndClassGroupDTO(Course course, ClassGroup classGroup) {
        this.course = course;
        this.classGroup = classGroup;
    }
}
