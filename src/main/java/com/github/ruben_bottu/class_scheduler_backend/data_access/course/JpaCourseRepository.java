package com.github.ruben_bottu.class_scheduler_backend.data_access.course;

import com.github.ruben_bottu.class_scheduler_backend.domain.course.Course;
import com.github.ruben_bottu.class_scheduler_backend.domain.course.CourseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCourseRepository extends JpaRepository<CourseEntity, Long>, CourseRepository {

    @SuppressWarnings("JpaQlInspection")
    @Override
    @Query("SELECT new Course(c.id, c.name) FROM CourseEntity c")
    List<Course> getAll();

    @Override
    int countByIdIn(List<Long> courseIds);
}
