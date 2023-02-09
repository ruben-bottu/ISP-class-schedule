package com.github.ruben_bottu.isp_class_schedule_backend.model;

import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;

public class SearchAlgorithmState {
    private final List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups; // Constant
    private final List<CourseAndClassGroupDTO> combination; // Add one element at each level

    private SearchAlgorithmState(List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups, List<CourseAndClassGroupDTO> combination) {
        this.coursesWithClassGroups = coursesWithClassGroups;
        this.combination = combination;
    }

    public SearchAlgorithmState(List<Pair<CourseDTO, List<ClassGroupDTO>>> coursesWithClassGroups) {
        this(coursesWithClassGroups, new ArrayList<>());
    }

    public int getDepth() {
        return combination.size();
    }

    public CourseAndClassGroupDTO getNewestElement() {
        return combination.get(combination.size() - 1);
    }

    public boolean isSolution() {
        return combination.size() == coursesWithClassGroups.size();
    }

    public List<SearchAlgorithmState> successors() {
        if (getDepth() == coursesWithClassGroups.size()) return Collections.emptyList();
        var courseWithClassGroups = coursesWithClassGroups.get(getDepth());
        var course = courseWithClassGroups.first;
        var classGroups = courseWithClassGroups.second;
        return classGroups.stream()
                .map(classGroup -> createSuccessorState(course, classGroup))
                .collect(Collectors.toList());
    }

    private SearchAlgorithmState createSuccessorState(CourseDTO course, ClassGroupDTO classGroup) {
        var shallowCopy = new ArrayList<>(combination);
        var courseAndClassGroup = new CourseAndClassGroupDTO(course, classGroup);
        shallowCopy.add(courseAndClassGroup);
        return new SearchAlgorithmState(coursesWithClassGroups, shallowCopy);
    }

    public SearchAlgorithmState sortByNumberOfClassGroupsAscending() {
        var shallowCopy = new ArrayList<>(coursesWithClassGroups);
        shallowCopy.sort(comparingInt(o -> o.second.size()));
        return new SearchAlgorithmState(shallowCopy, combination);
    }

    // ###################### GENERATED ######################

    public List<CourseAndClassGroupDTO> getCombination() {
        return combination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchAlgorithmState that = (SearchAlgorithmState) o;

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
