package isp_class_schedule_backend.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CourseNameClassGroupName {
    private String courseName;
    private String classGroupName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseNameClassGroupName that = (CourseNameClassGroupName) o;

        if (!Objects.equals(courseName, that.courseName)) return false;
        return Objects.equals(classGroupName, that.classGroupName);
    }

    @Override
    public int hashCode() {
        int result = courseName != null ? courseName.hashCode() : 0;
        result = 31 * result + (classGroupName != null ? classGroupName.hashCode() : 0);
        return result;
    }
}
