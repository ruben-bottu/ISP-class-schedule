package isp_class_schedule_backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "class_groups")
public class ClassGroup {

    @Id
    private int id;

    @NotBlank(message = "name.is.missing")
    private String name;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<Lesson> lessons;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassGroup that = (ClassGroup) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        return lessons.equals(that.lessons);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + lessons.hashCode();
        return result;
    }
}
