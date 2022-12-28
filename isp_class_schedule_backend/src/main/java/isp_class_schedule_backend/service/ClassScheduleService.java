package isp_class_schedule_backend.service;

import isp_class_schedule_backend.model.CourseRepository;
import isp_class_schedule_backend.model.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassScheduleService {

    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private LessonRepository lessonRepo;


    public String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds) {
        return lessonRepo.getCombinationsWithCollisionCountJson(rowLimit, courseIds);
    }
}
