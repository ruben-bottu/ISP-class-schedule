package com.github.ruben_bottu.isp_class_schedule_backend.model.lessons;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long>, CustomLessonRepository {

    @Query(value = "SELECT get_combinations_with_collision_count_json(?1, ?2)", nativeQuery = true)
    String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds);


    /*@Query(nativeQuery = true, value = """
        WITH selected_lessons AS (
            SELECT id, start_timestamp, end_timestamp
            FROM :course_id_class_group_ids INNER JOIN lessons USING (course_id, class_group_id)
        )
        SELECT count(*) / 2 AS collision_count
        FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
        WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
            (s2.start_timestamp, s2.end_timestamp);
    """)
    int countOverlaps(@Param("course_id_class_group_ids") String courseIdClassGroupIds);*/
}
