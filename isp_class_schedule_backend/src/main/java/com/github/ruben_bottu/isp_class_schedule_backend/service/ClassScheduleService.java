package com.github.ruben_bottu.isp_class_schedule_backend.service;

import com.github.ruben_bottu.isp_class_schedule_backend.model.*;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.Lesson;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClassScheduleService {
    private static final int MAX_NUMBER_OF_SOLUTIONS = 30;
    private final CourseRepository courseRepo;
    private final LessonRepository lessonRepo;

    public ClassScheduleService(CourseRepository courseRepo, LessonRepository lessonRepo) {
        this.courseRepo = courseRepo;
        this.lessonRepo = lessonRepo;
    }

    public String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds) {
        return lessonRepo.getCombinationsWithCollisionCountJson(rowLimit, courseIds);
    }

    public Iterable<Course> getAllCourses() {
        return courseRepo.findAll(Sort.unsorted());
    }

    public Iterable<Lesson> getAllLessons() {
        return lessonRepo.findAll(Sort.unsorted());
    }

    public int countOverlaps(List<CourseAndClassGroupDTO> list) {
        if (list.size() == 0) throw new IllegalStateException("List cannot be empty");
        if (list.size() == 1) return 0;
        return lessonRepo.countOverlaps(list);
    }

    private <T> boolean listContainsDuplicates(List<T> list) {
        Set<T> set = new HashSet<>();
        return !list.stream().allMatch(set::add);
    }

    private boolean allCourseIdsExist(List<Long> courseIds) {
        int numberOfIdsFound = courseRepo.countByIdIn(courseIds);
        return courseIds.size() == numberOfIdsFound;
    }

    public static IllegalArgumentException invalidCourseIdsException() {
        return new IllegalArgumentException("Invalid course IDs");
    }

    public List<ClassScheduleProposalDTO> getProposals(List<Long> courseIds, int requestedNumberOfSolutions) {
        if (requestedNumberOfSolutions < 0) throw new IllegalArgumentException("Number of solutions cannot be negative, given: " + requestedNumberOfSolutions);
        if (courseIds.isEmpty() || requestedNumberOfSolutions == 0) return Collections.emptyList();
        if (listContainsDuplicates(courseIds) || !allCourseIdsExist(courseIds)) throw invalidCourseIdsException();
        int numberOfSolutions = Math.min(requestedNumberOfSolutions, MAX_NUMBER_OF_SOLUTIONS);

        var coursesWithClassGroups = courseRepo.getCoursesWithClassGroups(courseIds);
        var algoState = new SearchAlgorithmState(coursesWithClassGroups);
        return Search.greedySearch(algoState, numberOfSolutions, this::countOverlaps);
    }
}
