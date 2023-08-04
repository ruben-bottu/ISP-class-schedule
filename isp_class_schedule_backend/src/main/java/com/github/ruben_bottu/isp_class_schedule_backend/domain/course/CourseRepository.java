package com.github.ruben_bottu.isp_class_schedule_backend.domain.course;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.course.CourseEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.course.CustomCourseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, CustomCourseRepository {

    @SuppressWarnings("JpaQlInspection")
    @Query("SELECT new Course(c.id, c.name) FROM CourseEntity c")
    List<Course> getAll();

    int countByIdIn(List<Long> courseIds);
}
