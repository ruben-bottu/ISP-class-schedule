package com.github.ruben_bottu.isp_class_schedule_backend.domain.class_;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.ClassEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.CustomClassRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long>, CustomClassRepository {

    @Query(value = "SELECT get_combinations_with_collision_count_json(?1, ?2)", nativeQuery = true)
    String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds);

    @Query(nativeQuery = true, value = """
            SELECT COUNT( DISTINCT EXTRACT(WEEK FROM c.start_timestamp) )
            FROM class as c INNER JOIN course_group cg on c.course_group_id = cg.id
            WHERE cg.course_id IN :courseIds
            """)
    int getNumberOfWeeksIn(List<Long> courseIds);

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

    // Is a native query because OVERLAPS is not supported by HQL/JPQL
    @Query(nativeQuery = true, value = """
            SELECT count(*) AS overlap_count
            FROM class c1 CROSS JOIN class c2
            WHERE c1.course_group_id = ?1 AND
                  c2.course_group_id IN (?2) AND
                  (c1.start_timestamp, c1.end_timestamp) OVERLAPS
                  (c2.start_timestamp, c2.end_timestamp);
            """)
    int countOverlapsBetween(Long courseGroupId, List<Long> courseGroupIds);

}
