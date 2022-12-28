package isp_class_schedule_backend.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.aspectj.runtime.internal.Conversions.intValue;

public class CustomLessonRepositoryImpl implements CustomLessonRepository {

    @PersistenceContext
    private EntityManager entityManager;

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
        return intValue( entityManager.createNativeQuery(query).getSingleResult() );
    }

    private String coursesAndClassGroupsToCourseIdAndClassGroupIdStrings(List<CourseAndClassGroupDTO> coursesAndClassGroups) {
        return coursesAndClassGroups.stream()
                .map(coAndClGr -> "("+coAndClGr.getCourse().getId()+","+coAndClGr.getClassGroup().getId()+")")
                .collect(Collectors.joining(","));
    }
}
