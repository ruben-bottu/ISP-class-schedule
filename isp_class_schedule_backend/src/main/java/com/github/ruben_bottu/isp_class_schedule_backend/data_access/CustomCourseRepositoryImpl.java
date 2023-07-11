package com.github.ruben_bottu.isp_class_schedule_backend.data_access;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassGroup;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.Pair;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;
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
    public List<Pair<Course, List<ClassGroup>>> getCoursesWithClassGroups(List<Long> courseIds) {
        Map<Long, Pair<Course, List<ClassGroup>>> coursesWithClassGroupsMap = new LinkedHashMap<>();

        @SuppressWarnings("unchecked")
        List<Pair<Course, List<ClassGroup>>> coursesWithClassGroups = entityManager.createQuery("""
        SELECT DISTINCT
                c.id AS cId,
                c.name AS cName,
                cg.id AS cgId,
                cg.name AS cgName
        FROM CourseEntity c JOIN LessonEntity l ON c.id = l.course.id
            JOIN ClassGroupEntity cg ON l.classGroup.id = cg.id
        WHERE c.id IN (:courseIds)
        """)
                .setParameter("courseIds", courseIds)
                .unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer((tuple, aliases) -> transformTuple(tuple, coursesWithClassGroupsMap))
                .setResultListTransformer(list -> transformList(coursesWithClassGroupsMap))
                .getResultList();

        return coursesWithClassGroups;
    }

    private Object transformTuple(Object[] tuple, Map<Long, Pair<Course, List<ClassGroup>>> courseDTOListMap) {

        Long courseId = (Long) tuple[0];
        String courseName = (String) tuple[1];
        Long classGroupId = (Long) tuple[2];
        String classGroupName = (String) tuple[3];

        Pair<Course, List<ClassGroup>> courseWithClassGroups = courseDTOListMap.computeIfAbsent(
                courseId,
                id -> Pair.of(new Course(courseId, courseName), new ArrayList<>())
        );

        courseWithClassGroups.second.add(new ClassGroup(classGroupId, classGroupName));

        return courseWithClassGroups;
    }

    private List<Pair<Course, List<ClassGroup>>> transformList(Map<Long, Pair<Course, List<ClassGroup>>> courseDTOListMap) {
        return new ArrayList<>(courseDTOListMap.values());
    }
}
