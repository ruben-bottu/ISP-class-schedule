package com.github.ruben_bottu.isp_class_schedule_backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

public class Search {

    /*public static List<ClassScheduleProposalDTO> greedySearch(SearchAlgorithmState startState, int numberOfSolutions, ToIntFunction<List<CourseAndClassGroupDTO>> countOverlaps) {
        NavigableSet<IntObjPair<SearchAlgorithmState>> fringe = new TreeSet<>(SearchAlgorithmFringeComparator.getInstance());
        fringe.add(IntObjPair.of(0, startState));
        List<ClassScheduleProposalDTO> result = new ArrayList<>();

        while (!fringe.isEmpty()) {
            var current = fringe.pollFirst();
            var state = current.object;
            if (state.isSolution()) {
                result.add(new ClassScheduleProposalDTO(current.integer, state.getCombination()));
                if (result.size() == numberOfSolutions) return result;
            }
            for (SearchAlgorithmState successor : state.successors()) {
                int overlapCount = countOverlaps.applyAsInt(successor.getCombination());
                fringe.add(IntObjPair.of(overlapCount, successor));
            }
        }
        return result;
    }*/

    public static List<ClassScheduleProposalDTO> greedySearch(SearchAlgorithmState startState, int numberOfSolutions, ToIntFunction<List<CourseAndClassGroupDTO>> countOverlaps) {
        // The set is sorted in ascending order
        var fringe = new TreeSet<>(SearchAlgorithmFringeComparator.getInstance());
        fringe.add(new SearchAlgorithmFringeDTO(0, startState));
        var result = new ArrayList<ClassScheduleProposalDTO>();

        while (!fringe.isEmpty()) {
            var current = fringe.pollFirst();
            var currentState = current.state();
            if (currentState.isSolution()) {
                result.add(new ClassScheduleProposalDTO(current.overlapCount(), currentState.getCombination()));
                if (result.size() == numberOfSolutions) return result;
            }
            for (SearchAlgorithmState successor : currentState.successors()) {
                int overlapCount = countOverlaps.applyAsInt(successor.getCombination());
                fringe.add(new SearchAlgorithmFringeDTO(overlapCount, successor));
            }
        }
        return result;
    }

    public static List<ClassScheduleProposalDTO> greedySearchMemory(SearchAlgorithmState startState, int numberOfSolutions, ToIntBiFunction<CourseAndClassGroupDTO, List<CourseAndClassGroupDTO>> countOverlapsBetween) {
        // The set is sorted in ascending order
        var fringe = new TreeSet<>(SearchAlgorithmFringeComparator.getInstance());
        fringe.add(new SearchAlgorithmFringeDTO(0, startState));
        var result = new ArrayList<ClassScheduleProposalDTO>();

        while (!fringe.isEmpty()) {
            var current = fringe.pollFirst();
            var currentState = current.state();
            if (currentState.isSolution()) {
                result.add(new ClassScheduleProposalDTO(current.overlapCount(), currentState.getCombination()));
                if (result.size() == numberOfSolutions) return result;
            }
            for (SearchAlgorithmState successor : currentState.successors()) {
                int overlapCount = current.overlapCount() + countOverlapsBetween.applyAsInt(successor.getNewestElement(), currentState.getCombination());
                fringe.add(new SearchAlgorithmFringeDTO(overlapCount, successor));
            }
        }
        return result;
    }
}
