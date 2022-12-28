package isp_class_schedule_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
// TODO remove
public class CourseIdClassGroupId {

    @Id // This annotation is just to keep Spring Boot happy
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "class_group_id")
    private Long classGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CourseIdClassGroupId that = (CourseIdClassGroupId) o;
        return courseId != null && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
