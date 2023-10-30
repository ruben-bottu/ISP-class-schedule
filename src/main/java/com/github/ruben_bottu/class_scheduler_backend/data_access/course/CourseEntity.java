package com.github.ruben_bottu.class_scheduler_backend.data_access.course;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "course")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "name.is.missing")
    @Column(unique = true)
    private String name;

    // https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
    /*@OneToMany(mappedBy = "classGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassEntity> classes;*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CourseEntity courseEntity = (CourseEntity) o;
        return id != null && Objects.equals(id, courseEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
