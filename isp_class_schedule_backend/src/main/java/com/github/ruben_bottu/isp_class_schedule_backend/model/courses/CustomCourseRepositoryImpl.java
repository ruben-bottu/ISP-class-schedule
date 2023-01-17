package com.github.ruben_bottu.isp_class_schedule_backend.model.courses;

import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassGroupDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.Pair;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Pair<CourseDTO, List<ClassGroupDTO>>> getCoursesWithClassGroups(List<Long> courseIds) {
        Map<Long, Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroupsMap = new LinkedHashMap<>();

        @SuppressWarnings("unchecked")
        List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups = entityManager.createQuery("""
        SELECT DISTINCT
                c.id AS c_id,
                c.name AS c_name,
                cg.id AS cg_id,
                cg.name AS cg_name
        FROM Course c JOIN Lesson l ON c.id = l.course.id
            JOIN ClassGroup cg ON l.classGroup.id = cg.id
        WHERE c.id IN (:courseIds)
        """)
                .setParameter("courseIds", courseIds)
                .unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer((tuple, aliases) -> transformTuple(tuple, coursesWithClassGroupsMap))
                .setResultListTransformer(list -> transformList(coursesWithClassGroupsMap))
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

        courseWithClassGroups.second.add(new ClassGroupDTO(classGroupId, classGroupName));

        return courseWithClassGroups;
    }

    private List<Pair<CourseDTO, List<ClassGroupDTO>>> transformList(Map<Long, Pair<CourseDTO, List<ClassGroupDTO>>> courseDTOListMap) {
        return new ArrayList<>(courseDTOListMap.values());
    }
}
