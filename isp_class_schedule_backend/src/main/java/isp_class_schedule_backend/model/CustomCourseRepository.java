package isp_class_schedule_backend.model;

import org.springframework.data.util.Pair;

import java.util.List;

public interface CustomCourseRepository {

    List<Pair<CourseDTO, List<ClassGroupDTO>>> getCoursesWithClassGroups();
}
