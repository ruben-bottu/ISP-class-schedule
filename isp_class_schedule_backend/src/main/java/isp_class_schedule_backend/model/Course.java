package isp_class_schedule_backend.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    private int id;

    @NotBlank(message = "name.is.missing")
    private String name;

    @OneToMany(mappedBy = "classGroup", fetch = FetchType.EAGER)
    private List<Lesson> lessons;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        if (!name.equals(course.name)) return false;
        return lessons.equals(course.lessons);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + lessons.hashCode();
        return result;
    }
}
