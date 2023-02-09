package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassScheduleProposalDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.Lesson;
import com.github.ruben_bottu.isp_class_schedule_backend.service.ClassScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.github.ruben_bottu.isp_class_schedule_backend.service.ClassScheduleService.invalidCourseIdsException;

@RestController
@RequestMapping("/api/schedule")
public class ClassScheduleRestController {
    public static final int DEFAULT_NUMBER_OF_SOLUTIONS = 20;
    private final ClassScheduleService service;

    public ClassScheduleRestController(ClassScheduleService service) {
        this.service = service;
    }

    @GetMapping("/class_schedule")
    public String getCombinationsWithCollisionCountJson(@RequestParam(name = "limit", defaultValue = DEFAULT_NUMBER_OF_SOLUTIONS + "") int resultLimit, @RequestBody List<Long> courseIds) {
        return service.getCombinationsWithCollisionCountJson(resultLimit, courseIds);
    }

    @GetMapping("/courses")
    public Iterable<Course> allCourses() {
        return service.getAllCourses();
    }

    @GetMapping("/lessons")
    public Iterable<Lesson> allLessons() {
        return service.getAllLessons();
    }

    private List<Long> stringToCourseIds(String string) {
        try {
            return Arrays.stream(string.split("\\+"))
                    .map(Long::parseLong)
                    .toList();
        } catch (NumberFormatException e) {
            throw invalidCourseIdsException();
        }
    }

    @GetMapping("search-tree")
    public String searchTreeToString() {
        return service.searchTreeToString();
    }

    @GetMapping("proposals/{rawCourseIds}")
    public List<ClassScheduleProposalDTO> proposals(@PathVariable String rawCourseIds, @RequestParam(name = "limit", defaultValue = DEFAULT_NUMBER_OF_SOLUTIONS + "") int requestedNumberOfSolutions) {
        var courseIds = stringToCourseIds(rawCourseIds);
        return service.getProposals(courseIds, requestedNumberOfSolutions);
    }
}
