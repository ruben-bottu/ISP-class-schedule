package com.github.ruben_bottu.isp_class_schedule_backend.model.courses;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.CustomCourseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long>, CustomCourseRepository {

    @Query("FROM Course")
    List<CourseDTO> getAll();

    int countByIdIn(List<Long> courseIds);
}
