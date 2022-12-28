package isp_class_schedule_backend.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.util.Pair;

import java.util.List;

public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // TODO NO setResultTransformer??
    @Override
    public List<Pair<CourseDTO, List<ClassGroupDTO>>> getCoursesWithClassGroups() {
        /*List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups = entityManager.createQuery("""
        SELECT c
        FROM Course c JOIN Lesson l ON c.id = l.course.id
        """).unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer()

        return coursesWithClassGroups;*/
        return null;
    }
}
