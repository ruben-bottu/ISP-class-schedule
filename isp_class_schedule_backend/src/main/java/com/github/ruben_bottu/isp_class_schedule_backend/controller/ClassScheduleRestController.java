package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassScheduleProposalDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.NotFoundException;
import com.github.ruben_bottu.isp_class_schedule_backend.model.ProposalsContract;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.Lesson;
import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassScheduleService;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/schedule")
public class ClassScheduleRestController {
    public static final int DEFAULT_SOLUTION_COUNT = 20;
    private final ClassScheduleService service;

    public ClassScheduleRestController(ClassScheduleService service) {
        this.service = service;
    }

    @GetMapping("/class_schedule")
    public String getCombinationsWithCollisionCountJson(@RequestParam(name = "limit", defaultValue = DEFAULT_SOLUTION_COUNT + "") int resultLimit, @RequestBody List<Long> courseIds) {
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

    @GetMapping("search-tree")
    public String searchTreeToString() {
        return service.searchTreeToString();
    }

    @GetMapping("proposals/{courseIds}")
    public List<ClassScheduleProposalDTO> proposals(@PathVariable List<Long> courseIds, @RequestParam(name = "count", defaultValue = DEFAULT_SOLUTION_COUNT + "") int solutionCount) {
        System.out.println(courseIds);
        var contract = new ProposalsContract(courseIds, solutionCount);
        return service.getProposals(contract);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handle(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();

        var violations = exception.getConstraintViolations();
        violations.forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, String> handle(NotFoundException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put(exception.getResourceName(), exception.getMessage());
        return errors;
    }
}
