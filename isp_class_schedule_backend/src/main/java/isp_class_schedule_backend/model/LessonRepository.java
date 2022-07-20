package isp_class_schedule_backend.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Integer> {

    @Query(value = "SELECT * FROM isp_class_schedule.lessons", nativeQuery = true)
    List<Lesson> getLessons();

    @Query(value = "SELECT course_id, class_group_id FROM isp_class_schedule.lessons", nativeQuery = true)
    List<?> getLessonIDs();

    @Query(nativeQuery = true, value = """
                    SELECT count(*)
                    FROM (VALUES ?1) AS sub
            """)
    int testMethod(List<Object[]> courseClassGroupIDs);

    // Since there isn't a pooling connection yet, selected_lessons won't be cleared at the end of the transaction (when the pooling connection closes)
    @Modifying
    @Query(nativeQuery = true, value = """
            CREATE TEMPORARY TABLE IF NOT EXISTS selected_lessons ON COMMIT DELETE ROWS AS
                SELECT id, start_timestamp, end_timestamp
                FROM isp_class_schedule.lessons s
                WHERE EXISTS (
                    SELECT '1'
                    FROM :course_class_group_ids i
                    WHERE i.course_id = s.course_id AND i.class_group_id = s.class_group_id
                );

            SELECT count(*) / 2 AS collision_count
            FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
            WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
                  (s2.start_timestamp, s2.end_timestamp)
            """)
    int oldCountCollisions(@Param("course_class_group_ids") List<CourseIdClassGroupId> courseClassGroupIDs);

    @Query(nativeQuery = true, value = """
        SELECT testRowConstr(?1)
    """)
    int countRows(String rowConstructors);

    @Query(value = "SELECT count_collisions(?1)", nativeQuery = true)
    int countCollisions(String courseIdsClassGroupIds);

    @Query(value = "SELECT get_combinations_with_collision_count_json(?1, ?2)", nativeQuery = true)
    String getCombinationsWithCollisionCountJson(int rowLimit, List<Integer> courseIds);

    /*@Query(value = "SELECT variadic_test(?1, ?2)", nativeQuery = true)
    String variadicTest(int firstParam, List<Integer> ints);*/
}
