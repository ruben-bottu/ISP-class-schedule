package com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleProposal;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseAndClassGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

public class Search {

    // Uses IntObjPair instead of dedicated type
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

    public List<ClassScheduleProposal> greedySearch(State startState, int solutionCount, ToIntFunction<List<CourseAndClassGroup>> countOverlaps) {
        // By first using the most "constrained" courses – the ones with the least class groups –
        // we can reduce the size of the tree significantly
        startState = startState.sortByClassGroupCountAscending();
        // The set is sorted in ascending order
        var fringe = new TreeSet<>(FringeComparator.getInstance());
        fringe.add(new Fringe(0, startState));
        var result = new ArrayList<ClassScheduleProposal>(solutionCount);

        while (!fringe.isEmpty()) {
            var current = fringe.pollFirst();
            var currentState = current.state();
            //System.err.println(currentState.getCombination().stream().map(coCg -> "("+coCg.course().id()+","+coCg.classGroup().id()+")").toList());
            if (currentState.isSolution()) {
                result.add(new ClassScheduleProposal(current.overlapCount(), currentState.getCombination()));
                if (result.size() == solutionCount) return result;
            }
            for (State successor : currentState.successors()) {
                int overlapCount = countOverlaps.applyAsInt(successor.getCombination());
                fringe.add(new Fringe(overlapCount, successor));
            }
        }
        return result;
    }

    private static int countOverlaps(ToIntBiFunction<CourseAndClassGroup, List<CourseAndClassGroup>> countOverlapsBetween, Fringe parent, State successor) {
        return parent.overlapCount() + countOverlapsBetween.applyAsInt(successor.getNewestElement(), parent.state().getCombination());
    }

    public static List<ClassScheduleProposal> greedySearchMemory(State startState, int solutionCount, ToIntBiFunction<CourseAndClassGroup, List<CourseAndClassGroup>> countOverlapsBetween) {
        // By first using the most "constrained" courses – the ones with the least class groups –
        // we can reduce the size of the tree significantly
        startState = startState.sortByClassGroupCountAscending();
        // The set is sorted in ascending order
        var fringe = new TreeSet<>(FringeComparator.getInstance());
        fringe.add(new Fringe(0, startState));
        var result = new ArrayList<ClassScheduleProposal>(solutionCount);

        while (!fringe.isEmpty()) {
            var current = fringe.pollFirst();
            var currentState = current.state();
            if (currentState.isSolution()) {
                result.add(new ClassScheduleProposal(current.overlapCount(), currentState.getCombination()));
                if (result.size() == solutionCount) return result;
            }
            for (State successor : currentState.successors()) {
                int overlapCount = countOverlaps(countOverlapsBetween, current, successor);
                fringe.add(new Fringe(overlapCount, successor));
            }
        }
        return result;
    }
}
