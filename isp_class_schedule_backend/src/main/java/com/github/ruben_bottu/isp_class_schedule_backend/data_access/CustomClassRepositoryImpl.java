package com.github.ruben_bottu.isp_class_schedule_backend.data_access;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

public class CustomClassRepositoryImpl implements CustomClassRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int countOverlaps(List<CourseGroup> list) {
        String courseGroupIds = courseGroupsToCourseGroupIdStrings(list);
        String query = """
                WITH selected_classes AS (
                    SELECT id, start_timestamp, end_timestamp
                    FROM (VALUES %s) AS i(course_id, group_id)
                        INNER JOIN course_group cg USING (course_id, group_id)
                        INNER JOIN class c ON cg.id = c.course_group_id
                )
                SELECT count(*) / 2 AS overlap_count
                FROM selected_classes s1 INNER JOIN selected_classes s2 ON s1.id <> s2.id
                WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
                    (s2.start_timestamp, s2.end_timestamp);
                """.formatted(courseGroupIds);
        return toInt(entityManager.createNativeQuery(query).getSingleResult());
    }

    private String courseGroupsToCourseGroupIdStrings(List<CourseGroup> coursesAndClassGroups) {
        return coursesAndClassGroups.stream()
                .map(coAndClGr -> "(" + coAndClGr.course().id() + "," + coAndClGr.group().id() + ")")
                .collect(Collectors.joining(","));
    }

    /*@Override
    public int countOverlapsBetween(CourseGroup element, List<CourseGroup> courseGroups) {
        List<Long> courseGroupIds = courseGroups.stream().map(CourseGroup::id).toList();
        String query = """
        SELECT count(*) AS overlap_count
        FROM (VALUES %s) AS i(course_id, group_id) INNER JOIN class c1 USING (course_id, group_id)
            INNER JOIN class c2 ON (c2.course_id, c2.group_id) = (:courseId, :groupId)
        WHERE (c1.start_timestamp, c1.end_timestamp) OVERLAPS
              (c2.start_timestamp, c2.end_timestamp);
                """.formatted(courseGroupIds);
        Object result = entityManager.createNativeQuery(query)
                .setParameter("courseId", element.course().id())
                .setParameter("groupId", element.group().id())
                .getSingleResult();
        return toInt(result);
    }*/

    @Override
    public int countOverlapsBetween(CourseGroup courseGroup, List<CourseGroup> courseGroups) {
        List<Long> courseGroupIds = courseGroups.stream().map(CourseGroup::id).toList();

        // Needs to be a native query because OVERLAPS is not supported by JPQL/HQL
        String query = """
                SELECT count(*) AS overlap_count
                FROM class c1 INNER JOIN class c2 ON c1.course_group_id = :courseGroupId
                WHERE c2.course_group_id IN (:courseGroupIds) AND
                      (c1.start_timestamp, c1.end_timestamp) OVERLAPS
                      (c2.start_timestamp, c2.end_timestamp);
                        """;
        Object result = entityManager.createNativeQuery(query)
                .setParameter("courseGroupId", courseGroup.id())
                .setParameter("courseGroupIds", courseGroupIds)
                .getSingleResult();
        return toInt(result);
    }

    private int toInt(Object o) {
        return ((Long) o).intValue();
    }
}
