package com.github.ruben_bottu.isp_class_schedule_backend;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleService;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm.Search;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.class_.ClassRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course_group.CourseGroupRepository;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

@Configuration
public class DomainConfiguration {

    @Bean
    public Search search() {
        return new Search();
    }

    @Bean
    public ClassScheduleService classScheduleService(CourseRepository courseRepo, CourseGroupRepository courseGroupRepo, ClassRepository classRepo, Validator validator, Search search) {
        return new ClassScheduleService(courseRepo, courseGroupRepo, classRepo, validator, search);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
