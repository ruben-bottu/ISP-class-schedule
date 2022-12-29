package isp_class_schedule_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    private Long id;

    @NotNull(message = "start.timestamp.missing")
    @Column(name = "start_timestamp")
    private LocalDateTime startTimestamp;

    @NotNull(message = "end.timestamp.missing")
    @Column(name = "end_timestamp")
    private LocalDateTime endTimestamp;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id") // Should not be necessary
    private Course course;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_group_id", referencedColumnName = "id") // Should not be necessary
    private ClassGroup classGroup;

    public String getCourseName() {
        return (course == null) ? "" : course.getName();
    }

    public String getClassGroupName() {
        return (classGroup == null) ? "" : classGroup.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Lesson lesson = (Lesson) o;
        return id != null && Objects.equals(id, lesson.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
