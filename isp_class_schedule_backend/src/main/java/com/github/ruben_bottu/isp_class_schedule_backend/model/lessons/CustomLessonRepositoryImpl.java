package com.github.ruben_bottu.isp_class_schedule_backend.model.lessons;

import com.github.ruben_bottu.isp_class_schedule_backend.model.CourseAndClassGroupDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

public class CustomLessonRepositoryImpl implements CustomLessonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // TODO use Hibernate Query Language
    @Override
    public int countOverlaps(List<CourseAndClassGroupDTO> list) {
        String courseIdAndClassGroupIds = coursesAndClassGroupsToCourseIdAndClassGroupIdStrings(list);
        String query = """
        WITH selected_lessons AS (
            SELECT id, start_timestamp, end_timestamp
            FROM (VALUES %s) AS i(course_id, class_group_id)
                INNER JOIN lessons USING (course_id, class_group_id)
        )
        SELECT count(*) / 2 AS overlap_count
        FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
        WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
            (s2.start_timestamp, s2.end_timestamp);
        """.formatted(courseIdAndClassGroupIds);
        return toInt( entityManager.createNativeQuery(query).getSingleResult() );
    }

    private int toInt(Object o) {
        return ((Long) o).intValue();
    }

    private String coursesAndClassGroupsToCourseIdAndClassGroupIdStrings(List<CourseAndClassGroupDTO> coursesAndClassGroups) {
        return coursesAndClassGroups.stream()
                .map(coAndClGr -> "("+coAndClGr.course().id()+","+coAndClGr.classGroup().id()+")")
                .collect(Collectors.joining(","));
    }
}
