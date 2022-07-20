package isp_class_schedule_backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class CourseIdClassGroupId {

    @Id // This annotation is just to keep Spring Boot happy
    @Column(name = "course_id")
    private int courseId;

    @Column(name = "class_group_id")
    private int classGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseIdClassGroupId that = (CourseIdClassGroupId) o;

        if (courseId != that.courseId) return false;
        return classGroupId == that.classGroupId;
    }

    @Override
    public int hashCode() {
        int result = courseId;
        result = 31 * result + classGroupId;
        return result;
    }

    @Override
    public String toString() {
        return "(" + courseId + "," + classGroupId + ')';
    }
}
