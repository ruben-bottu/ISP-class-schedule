package com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleProposal;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

public class Search {

    public List<ClassScheduleProposal> greedySearch(State startState, int solutionCount, ToIntFunction<List<CourseGroup>> countOverlaps) {
        // By first using the most "constrained" courses – the ones with the least courseGroups –
        // we can reduce the size of the tree significantly
        startState = startState.sortByCourseGroupCountAscending();
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

    private static int countOverlaps(ToIntBiFunction<CourseGroup, List<CourseGroup>> countOverlapsBetween, Fringe parent, State successor) {
        return parent.overlapCount() + countOverlapsBetween.applyAsInt(successor.getNewestElement(), parent.state().getCombination());
    }

    public List<ClassScheduleProposal> greedySearchMemory(State startState, int solutionCount, ToIntBiFunction<CourseGroup, List<CourseGroup>> countOverlapsBetween) {
        // By first using the most "constrained" courses – the ones with the least courseGroups –
        // we can reduce the size of the tree significantly
        startState = startState.sortByCourseGroupCountAscending();
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
