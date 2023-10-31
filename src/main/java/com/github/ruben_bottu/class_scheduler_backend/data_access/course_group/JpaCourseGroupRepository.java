package com.github.ruben_bottu.class_scheduler_backend.data_access.course_group;

import com.github.ruben_bottu.class_scheduler_backend.domain.course_group.CourseGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCourseGroupRepository extends JpaRepository<CourseGroupEntity, Long>, CourseGroupRepository {
    int countByIdIn(List<Long> courseGroupIds);
}
