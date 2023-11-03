package com.github.ruben_bottu.class_scheduler_backend.controller;

import com.github.ruben_bottu.class_scheduler_backend.ClassScheduleConfigurationProperties;
import com.github.ruben_bottu.class_scheduler_backend.controller.dto.ClassDTO;
import com.github.ruben_bottu.class_scheduler_backend.controller.dto.CourseDTO;
import com.github.ruben_bottu.class_scheduler_backend.controller.dto.ProposalDTO;
import com.github.ruben_bottu.class_scheduler_backend.domain.ClassScheduleProperties;
import com.github.ruben_bottu.class_scheduler_backend.domain.ClassScheduleService;
import com.github.ruben_bottu.class_scheduler_backend.domain.NotFoundException;
import com.github.ruben_bottu.class_scheduler_backend.domain.validation.IdListContract;
import com.github.ruben_bottu.class_scheduler_backend.domain.validation.MaxSizeConstraintValidator;
import com.github.ruben_bottu.class_scheduler_backend.domain.validation.ProposalsContract;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/class-schedule")
public class ClassScheduleRestController {
    private final ClassScheduleService service;
    private final ClassScheduleProperties properties;
    private final DecimalFormat threeDecimalPlaces;

    public ClassScheduleRestController(ClassScheduleService service, ClassScheduleConfigurationProperties properties) {
        this.service = service;
        this.properties = mapToDomain(properties);
        MaxSizeConstraintValidator.setMaxSize(properties.maxIdListSize());
        threeDecimalPlaces = new DecimalFormat("###.###");
    }

    // ClassScheduleProperties (domain) should not know about ClassScheduleConfigurationProperties
    private static ClassScheduleProperties mapToDomain(ClassScheduleConfigurationProperties properties) {
        return new ClassScheduleProperties(properties.defaultSolutionCount(), properties.maxSolutionCount(), properties.maxIdListSize());
    }

    // Legacy proposals method
    @GetMapping("/class_schedule")
    public String getCombinationsWithCollisionCountJson(@RequestParam(name = "limit", defaultValue = "#{'${class-schedule.default-solution-count}'}") int resultLimit, @RequestBody List<Long> courseIds) {
        return service.getCombinationsWithCollisionCountJson(resultLimit, courseIds);
    }

    @GetMapping("/courses")
    public List<CourseDTO> allCourses() {
        return service.getAllCourses().stream().map(CourseDTO::new).toList();
    }

    @GetMapping("/course-groups/{courseGroupIds}/classes")
    public List<ClassDTO> getClassesByCourseGroupIdIn(@PathVariable List<Long> courseGroupIds) {
        var contract = new IdListContract(courseGroupIds);
        return service.getClassesByCourseGroupIdIn(contract).stream().map(ClassDTO::new).toList();
    }

    @GetMapping("search-tree")
    public String searchTreeToString() {
        return service.searchTreeToString();
    }

    private String divide(int dividend, int divisor) {
        var quotient = (float) dividend / divisor;
        return threeDecimalPlaces.format(quotient);
    }

    @GetMapping("proposals/{courseIds}")
    public List<ProposalDTO> proposals(@PathVariable List<Long> courseIds, @RequestParam(name = "count", defaultValue = "-1") int solutionCount) {
        var contract = new ProposalsContract(courseIds, solutionCount, properties);
        var weekCount = service.countWeeksIn(courseIds);
        return service.getProposals(contract).stream()
                .map((p) -> new ProposalDTO(divide(p.overlapCount(), weekCount), ProposalDTO.mapToDto(p.combination())))
                .toList();
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
