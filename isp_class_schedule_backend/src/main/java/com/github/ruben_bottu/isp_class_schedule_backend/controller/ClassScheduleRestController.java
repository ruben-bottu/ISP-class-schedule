package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import com.github.ruben_bottu.isp_class_schedule_backend.ClassScheduleConfigurationProperties;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.ClassEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.course.CourseEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.*;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.validation.MaxSizeConstraintValidator;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.validation.ProposalsContract;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/class-schedule")
public class ClassScheduleRestController {
    private final ClassScheduleService service;
    private final ClassScheduleProperties properties;

    public ClassScheduleRestController(ClassScheduleService service, ClassScheduleConfigurationProperties properties) {
        this.service = service;
        this.properties = mapToDomain(properties);
        MaxSizeConstraintValidator.maxSize = properties.maxCourseIdsSize();
    }

    private static ClassScheduleProperties mapToDomain(ClassScheduleConfigurationProperties properties) {
        return new ClassScheduleProperties(properties.defaultSolutionCount(), properties.maxSolutionCount(), properties.maxCourseIdsSize());
    }

    @GetMapping("/class_schedule")
    public String getCombinationsWithCollisionCountJson(@RequestParam(name = "limit", defaultValue = "#{'${class-schedule.default-solution-count}'}") int resultLimit, @RequestBody List<Long> courseIds) {
        return service.getCombinationsWithCollisionCountJson(resultLimit, courseIds);
    }

    @GetMapping("/courses")
    public Iterable<CourseEntity> allCourses() {
        return service.getAllCourses();
    }

    @GetMapping("/classes")
    public Iterable<ClassEntity> allClasses() {
        return service.getAllClasses();
    }

    /*@GetMapping("search-tree")
    public String searchTreeToString() {
        return service.searchTreeToString();
    }*/

    @GetMapping("proposals/{courseIds}")
    public List<ClassScheduleProposal> proposals(@PathVariable List<Long> courseIds, @RequestParam(name = "count", required = false) Integer solutionCount) {
        var contract = new ProposalsContract(courseIds, solutionCount, properties);
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
