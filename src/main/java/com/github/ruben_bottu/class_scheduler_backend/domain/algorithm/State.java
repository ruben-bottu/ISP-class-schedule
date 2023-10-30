package com.github.ruben_bottu.class_scheduler_backend.domain.algorithm;

import com.github.ruben_bottu.class_scheduler_backend.domain.course_group.CourseGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class State {
    private final List<List<CourseGroup>> courseGroupsGroupedByCourse; // Readonly
    private final List<CourseGroup> combination; // Add one element at each level of the tree

    private State(List<List<CourseGroup>> courseGroupsGroupedByCourse, List<CourseGroup> combination) {
        this.courseGroupsGroupedByCourse = courseGroupsGroupedByCourse;
        this.combination = combination;
    }

    public State(List<List<CourseGroup>> courseGroupsGroupedByCourse) {
        this(courseGroupsGroupedByCourse, new ArrayList<>());
    }

    public int getDepth() {
        return combination.size();
    }

    public CourseGroup getNewestElement() {
        return combination.get(combination.size() - 1);
    }

    public boolean isSolution() {
        return combination.size() == courseGroupsGroupedByCourse.size();
    }

    public List<State> successors() {
        if (getDepth() == courseGroupsGroupedByCourse.size()) return Collections.emptyList();
        var courseGroups = courseGroupsGroupedByCourse.get(getDepth());
        return courseGroups.stream()
                .map(this::createSuccessorState)
                .toList();
    }

    private State createSuccessorState(CourseGroup courseGroup) {
        var shallowCopy = new ArrayList<>(combination);
        shallowCopy.add(courseGroup);
        return new State(courseGroupsGroupedByCourse, shallowCopy);
    }

    public State sortByCourseGroupCountAscending() {
        var shallowCopy = new ArrayList<>(courseGroupsGroupedByCourse);
        shallowCopy.sort(comparingInt(List::size));
        return new State(shallowCopy, combination);
    }


    // ######################
    //      Generated       #
    // ######################

    public List<CourseGroup> getCombination() {
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
        return "State{" +
                "combination=" + combination +
                '}';
    }
}
