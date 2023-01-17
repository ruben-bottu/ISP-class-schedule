package com.github.ruben_bottu.isp_class_schedule_backend.model;

import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ClassScheduleAlgoState {
    private final List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups;
    @EqualsAndHashCode.Include
    private final List<CourseAndClassGroupDTO> combination; // Add one element at each level
    private final int currentIndex;

    /*private ClassScheduleAlgoState(List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups, List<CourseAndClassGroupDTO> combination, int currentIndex) {
        this.coursesWithClassGroups = coursesWithClassGroups;
        this.combination = combination;
        this.currentIndex = currentIndex;
    }*/

    public ClassScheduleAlgoState(List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups) {
        this(coursesWithClassGroups, new ArrayList<>(), 0);
    }

    public List<CourseAndClassGroupDTO> getCombination() {
        return combination;
    }

    public int getDepth() {
        return combination.size();
    }

    public boolean isSolution() {
        return coursesWithClassGroups.size() == combination.size();
    }

    public List<ClassScheduleAlgoState> successors() {
        if (currentIndex == coursesWithClassGroups.size()) return Collections.emptyList();
        var courseWithClassGroups = coursesWithClassGroups.get(currentIndex);
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
        return new ClassScheduleAlgoState(coursesWithClassGroups, shallowCopy, currentIndex + 1);
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassScheduleAlgoState that = (ClassScheduleAlgoState) o;

        return combination.equals(that.combination);
    }

    @Override
    public int hashCode() {
        return combination.hashCode();
    }*/

    /*@Override
    public String toString() {
        return "ClassScheduleAlgoState{" +
                "coursesWithClassGroups=" + coursesWithClassGroups +
                ", combination=" + combination +
                ", currentIndex=" + currentIndex +
                '}';
    }*/
}
