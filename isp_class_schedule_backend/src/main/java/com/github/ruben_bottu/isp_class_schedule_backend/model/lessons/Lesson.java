package com.github.ruben_bottu.isp_class_schedule_backend.model.lessons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassGroup;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id") // Should not be necessary
    private Course course;

    @JsonBackReference
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

    // ###################### GENERATED ######################

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", courseName=" + getCourseName() +
                ", classGroupName=" + getClassGroupName() +
                '}';
    }
}
