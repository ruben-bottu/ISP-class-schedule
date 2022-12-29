package isp_class_schedule_backend.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Pair<CourseDTO, List<ClassGroupDTO>>> getCoursesWithClassGroups() {
        Map<Long, Pair<CourseDTO, List<ClassGroupDTO>>> courseDTOListMap = new LinkedHashMap<>();

        @SuppressWarnings("unchecked")
        List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups = entityManager.createQuery("""
        SELECT DISTINCT
                c.id AS c_id,
                c.name AS c_name,
                cg.id AS cg_id,
                cg.name AS cg_name
        FROM Course c JOIN Lesson l ON c.id = l.course.id
            JOIN ClassGroup cg ON l.classGroup.id = cg.id
        """)
                .unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer((tuple, aliases) -> transformTuple(tuple, courseDTOListMap))
                .setResultListTransformer(list -> transformList(courseDTOListMap))
                .getResultList();

        return coursesWithClassGroups;
    }

    private Object transformTuple(Object[] tuple, Map<Long, Pair<CourseDTO, List<ClassGroupDTO>>> courseDTOListMap) {

        Long courseId = (Long) tuple[0];
        String courseName = (String) tuple[1];
        Long classGroupId = (Long) tuple[2];
        String classGroupName = (String) tuple[3];

        Pair<CourseDTO, List<ClassGroupDTO>> courseWithClassGroups = courseDTOListMap.computeIfAbsent(
                courseId,
                id -> Pair.of(new CourseDTO(courseId, courseName), new ArrayList<>())
        );

        courseWithClassGroups.getSecond().add(new ClassGroupDTO(classGroupId, classGroupName));

        return courseWithClassGroups;
    }

    private List<Pair<CourseDTO, List<ClassGroupDTO>>> transformList(Map<Long, Pair<CourseDTO, List<ClassGroupDTO>>> courseDTOListMap) {
        return new ArrayList<>(courseDTOListMap.values());
    }


}
