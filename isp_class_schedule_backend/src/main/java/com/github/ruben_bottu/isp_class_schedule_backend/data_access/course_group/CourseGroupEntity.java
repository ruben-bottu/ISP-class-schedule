package com.github.ruben_bottu.isp_class_schedule_backend.data_access.course_group;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.GroupEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.course.CourseEntity;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(
        name = "course_group",
        uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "group_id" })
)
public class CourseGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id") // Should not be necessary
    private CourseEntity course;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id") // Should not be necessary
    private GroupEntity group;

    public String getCourseName() {
        return (course == null) ? "" : course.getName();
    }

    public String getGroupName() {
        return (group == null) ? "" : group.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CourseGroupEntity courseGroupEntity = (CourseGroupEntity) o;
        return id != null && Objects.equals(id, courseGroupEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "CourseGroupEntity{" +
                "id=" + id +
                ", courseName=" + getCourseName() +
                ", groupName=" + getGroupName() +
                '}';
    }


    // ######################
    //      Generated       #
    // ######################

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }
}
