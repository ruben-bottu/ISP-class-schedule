package com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassGroup;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseAndClassGroup;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.Pair;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

public class State {
    private final List<Pair<Course, List<ClassGroup>>> coursesWithClassGroups; // Constant
    private final List<CourseAndClassGroup> combination; // Add one element at each level

    private State(List<Pair<Course, List<ClassGroup>>> coursesWithClassGroups, List<CourseAndClassGroup> combination) {
        this.coursesWithClassGroups = coursesWithClassGroups;
        this.combination = combination;
    }

    public State(List<Pair<Course, List<ClassGroup>>> coursesWithClassGroups) {
        this(coursesWithClassGroups, new ArrayList<>());
    }

    public int getDepth() {
        return combination.size();
    }

    public CourseAndClassGroup getNewestElement() {
        return combination.get(combination.size() - 1);
    }

    public boolean isSolution() {
        return combination.size() == coursesWithClassGroups.size();
    }

    public List<State> successors() {
        if (getDepth() == coursesWithClassGroups.size()) return Collections.emptyList();
        var courseWithClassGroups = coursesWithClassGroups.get(getDepth());
        var course = courseWithClassGroups.first;
        var classGroups = courseWithClassGroups.second;
        return classGroups.stream()
                .map(classGroup -> createSuccessorState(course, classGroup))
                .collect(Collectors.toList());
    }

    private State createSuccessorState(Course course, ClassGroup classGroup) {
        var shallowCopy = new ArrayList<>(combination);
        var courseAndClassGroup = new CourseAndClassGroup(course, classGroup);
        shallowCopy.add(courseAndClassGroup);
        return new State(coursesWithClassGroups, shallowCopy);
    }

    public State sortByClassGroupCountAscending() {
        var shallowCopy = new ArrayList<>(coursesWithClassGroups);
        shallowCopy.sort(comparingInt(o -> o.second.size()));
        return new State(shallowCopy, combination);
    }

    // ###################### GENERATED ######################

    public List<CourseAndClassGroup> getCombination() {
        return combination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State that = (State) o;

        return combination.equals(that.combination);
    }

    @Override
    public int hashCode() {
        return combination.hashCode();
    }

    @Override
    public String toString() {
        return "SearchAlgorithmState{" +
                "combination=" + combination +
                '}';
    }
}
