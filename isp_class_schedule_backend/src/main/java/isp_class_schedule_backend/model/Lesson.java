package isp_class_schedule_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    private int id;

    @NotNull(message = "start.timestamp.missing")
    @Column(name = "start_timestamp")
    private LocalDateTime startTimestamp;

    @NotNull(message = "end.timestamp.missing")
    @Column(name = "end_timestamp")
    private LocalDateTime endTimestamp;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id") // Should not be necessary
    @ToString.Exclude
    private Course course;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_group_id", referencedColumnName = "id") // Should not be necessary
    @ToString.Exclude
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
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        return id == lesson.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
