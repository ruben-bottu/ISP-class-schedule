package com.github.ruben_bottu.isp_class_schedule_backend.domain.class_;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long>/*, CustomClassRepository*/ {

    @Query(value = "SELECT get_combinations_with_collision_count_json(?1, ?2)", nativeQuery = true)
    String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds);

    @Query(nativeQuery = true, value = """
            SELECT COUNT( DISTINCT EXTRACT(WEEK FROM c.start_timestamp) )
            FROM class as c INNER JOIN course_group cg on c.course_group_id = cg.id
            WHERE cg.course_id IN :courseIds
            """)
    int getNumberOfWeeksIn(List<Long> courseIds);

    // Needs to be a native query because WITH and OVERLAPS are not supported by JPQL/HQL
    // For every class we want to check whether it overlaps with any other class. Only the classes that take place in the given course groups are selected for this calculation.
    // Result needs to be divided by two because the carthesian product makes the same overlap be counted twice
    // e.g. if class1 and class2 overlap, then both (class1, class2) and (class2, class1) will be counted
    @Query(nativeQuery = true, value = """
            WITH selected_classes AS (
                SELECT c.id, start_timestamp, end_timestamp
                FROM course_group cg INNER JOIN class c ON cg.id = c.course_group_id
                WHERE cg.id IN (?1)
            )
            SELECT count(*) / 2 AS overlap_count
            FROM selected_classes c1 INNER JOIN selected_classes c2 ON c1.id <> c2.id
            WHERE (c1.start_timestamp, c1.end_timestamp) OVERLAPS
                  (c2.start_timestamp, c2.end_timestamp);
            """)
    int countOverlaps(List<Long> courseGroupIds);

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
