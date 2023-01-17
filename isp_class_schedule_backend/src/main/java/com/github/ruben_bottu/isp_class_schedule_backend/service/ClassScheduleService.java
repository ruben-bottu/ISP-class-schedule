package com.github.ruben_bottu.isp_class_schedule_backend.service;

import com.github.ruben_bottu.isp_class_schedule_backend.model.*;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.Lesson;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<Lesson> getLessons() {
        return lessonRepo.getLessons();
    }

    public Iterable<Course> getAllCourses() {
        return courseRepo.findAll(Sort.unsorted());
    }

    public int countOverlaps(List<CourseAndClassGroupDTO> list) {
        if (list.size() == 0) throw new IllegalStateException("List cannot be empty");
        if (list.size() == 1) return 0;
        return lessonRepo.countOverlaps(list);
    }

    public List<ClassScheduleProposalDTO> getProposals(int numberOfSolutions, List<Long> courseIds) {
        var coursesWithClassGroups = courseRepo.getCoursesWithClassGroups(courseIds);
        var algoState = new ClassScheduleAlgoState(coursesWithClassGroups);
        return ClassScheduleSearch.searchGreedy(algoState, numberOfSolutions, this::countOverlaps);
    }
}
