package com.github.ruben_bottu.isp_class_schedule_backend.model;

import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ClassScheduleAlgoState {
    private final List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups; // Immutable
    @EqualsAndHashCode.Include
    @ToString.Include
    private final List<CourseAndClassGroupDTO> combination; // Add one element at each level

    public ClassScheduleAlgoState(List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups) {
        this(coursesWithClassGroups, new ArrayList<>());
    }

    public List<CourseAndClassGroupDTO> getCombination() {
        return combination;
    }

    public int getDepth() {
        return combination.size();
    }

    public boolean isSolution() {
        return combination.size() == coursesWithClassGroups.size();
    }

    public List<ClassScheduleAlgoState> successors() {
        if (getDepth() == coursesWithClassGroups.size()) return Collections.emptyList();
        var courseWithClassGroups = coursesWithClassGroups.get(getDepth());
        var course = courseWithClassGroups.first;
        var classGroups = courseWithClassGroups.second;
        return classGroups.stream()
                .map(classGroup -> createSuccessorState(course, classGroup))
                .collect(Collectors.toList());
    }

    private ClassScheduleAlgoState createSuccessorState(CourseDTO course, ClassGroupDTO classGroup) {
        var shallowCopy = new ArrayList<>(combination);
        var courseAndClassGroup = new CourseAndClassGroupDTO(course, classGroup);
        shallowCopy.add(courseAndClassGroup);
        return new ClassScheduleAlgoState(coursesWithClassGroups, shallowCopy);
    }
}
