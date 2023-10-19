package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import com.github.ruben_bottu.isp_class_schedule_backend.ClassScheduleConfigurationProperties;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleProperties;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleProposal;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleService;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.NotFoundException;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.class_.ClassSummary;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;
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
        MaxSizeConstraintValidator.setMaxSize(properties.maxCourseIdsSize());
    }

    private static ClassScheduleProperties mapToDomain(ClassScheduleConfigurationProperties properties) {
        return new ClassScheduleProperties(properties.defaultSolutionCount(), properties.maxSolutionCount(), properties.maxCourseIdsSize());
    }

    @GetMapping("/class_schedule")
    public String getCombinationsWithCollisionCountJson(@RequestParam(name = "limit", defaultValue = "#{'${class-schedule.default-solution-count}'}") int resultLimit, @RequestBody List<Long> courseIds) {
        return service.getCombinationsWithCollisionCountJson(resultLimit, courseIds);
    }

    @GetMapping("/courses")
    public List<Course> allCourses() {
        return service.getAllCourses();
    }

    @GetMapping("/course-groups/{courseGroupIds}/classes")
    public List<ClassSummary> getClassesByCourseGroupIdIn(@PathVariable List<Long> courseGroupIds) {
        return service.getClassesByCourseGroupIdIn(courseGroupIds);
    }

    @GetMapping("search-tree")
    public String searchTreeToString() {
        return service.searchTreeToString();
    }

    @GetMapping("proposals/{courseIds}")
    public List<ClassScheduleProposal> proposals(@PathVariable List<Long> courseIds, @RequestParam(name = "count", defaultValue = "-1") int solutionCount) {
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
