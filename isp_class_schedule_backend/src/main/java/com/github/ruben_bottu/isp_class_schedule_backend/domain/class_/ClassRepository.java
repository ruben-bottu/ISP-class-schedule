package com.github.ruben_bottu.isp_class_schedule_backend.domain.class_;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

    @Query("""
         SELECT new ClassSummary(cl.startTimestamp, cl.endTimestamp, c.name, g.name)
         FROM ClassEntity cl join cl.courseGroup cg join cg.course c join cg.group g
         WHERE cg.id IN :courseGroupIds
         """)
    List<ClassSummary> getByCourseGroupIdIn(@Param("courseGroupIds") List<Long> courseGroupIds);

    @Query(value = "SELECT get_combinations_with_collision_count_json(?1, ?2)", nativeQuery = true)
    String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds);

    // Native query because EXTRACT(WEEK ...) is not supported by JPQL/HQL
    @Query(nativeQuery = true, value = """
            SELECT COUNT( DISTINCT EXTRACT(WEEK FROM c.start_timestamp) )
            FROM course_group cg INNER JOIN class as c on cg.id = c.course_group_id
            WHERE cg.course_id IN :courseIds
            """)
    int countWeeksIn(@Param("courseIds") List<Long> courseIds);

    // Native query because WITH and OVERLAPS are not supported by JPQL/HQL
    // For every class we want to check whether it overlaps with any other class. Only the classes that take place in the given course groups are selected for this calculation.
    // Result needs to be divided by two because the carthesian product makes the same overlap be counted twice
    // e.g. if class1 and class2 overlap, then both (class1, class2) and (class2, class1) will be counted
    @Query(nativeQuery = true, value = """
            WITH selected_classes AS (
                SELECT c.id, start_timestamp, end_timestamp
                FROM course_group cg INNER JOIN class c ON cg.id = c.course_group_id
                WHERE cg.id IN (:courseGroupIds)
            )
            SELECT count(*) / 2 AS overlap_count
            FROM selected_classes c1 INNER JOIN selected_classes c2 ON c1.id <> c2.id
            WHERE (c1.start_timestamp, c1.end_timestamp) OVERLAPS
                  (c2.start_timestamp, c2.end_timestamp);
            """)
    int countOverlaps(@Param("courseGroupIds") List<Long> courseGroupIds);

    // Native query because OVERLAPS is not supported by JPQL/HQL
    @Query(nativeQuery = true, value = """
            SELECT count(*) AS overlap_count
            FROM class c1 CROSS JOIN class c2
            WHERE c1.course_group_id = :courseGroupId AND
                  c2.course_group_id IN (:courseGroupIds) AND
                  (c1.start_timestamp, c1.end_timestamp) OVERLAPS
                  (c2.start_timestamp, c2.end_timestamp);
            """)
    int countOverlapsBetween(@Param("courseGroupId") Long courseGroupId, @Param("courseGroupIds") List<Long> courseGroupIds);
}
