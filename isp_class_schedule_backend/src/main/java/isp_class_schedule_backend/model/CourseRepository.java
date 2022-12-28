package isp_class_schedule_backend.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long>, CustomCourseRepository {

    /*@Query(value = "SELECT CAST (pg_typeof(?1) AS text)", nativeQuery = true)
    String testMethod(List<Course> strings);*/



    @Query(nativeQuery = true, value = """
            CREATE TEMPORARY TABLE IF NOT EXISTS selected_lessons AS
                SELECT id, start_timestamp, end_timestamp
                FROM lessons s
                WHERE EXISTS (
                    SELECT '1'
                    FROM input_data i
                    WHERE i.course_id = s.course_id AND i.class_group_id = s.class_group_id
            );

            SELECT count(*) / 2 AS collision_count
            FROM selected_lessons s1 INNER JOIN selected_lessons s2 ON s1.id <> s2.id
            WHERE (s1.start_timestamp, s1.end_timestamp) OVERLAPS
                  (s2.start_timestamp, s2.end_timestamp)
            """)
    String testMethod(List<String> strings);

}
