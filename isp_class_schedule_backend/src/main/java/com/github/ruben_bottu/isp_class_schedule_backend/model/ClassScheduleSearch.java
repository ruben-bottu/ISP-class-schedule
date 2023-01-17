package com.github.ruben_bottu.isp_class_schedule_backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.function.ToIntFunction;

// TODO use strategy pattern
public class ClassScheduleSearch {

    public static List<ClassScheduleProposalDTO> searchGreedy(ClassScheduleAlgoState root, int numberOfSolutions, ToIntFunction<List<CourseAndClassGroupDTO>> countOverlaps) {
        NavigableSet<IntObjPair<ClassScheduleAlgoState>> fringe = new TreeSet<>(ClassScheduleFringeComparator.getInstance());
        fringe.add(IntObjPair.of(0, root));
        List<ClassScheduleProposalDTO> result = new ArrayList<>();

        while (!fringe.isEmpty()) {
            var current = fringe.pollFirst();
            var state = current.object;
            if (state.isSolution()) {
                result.add(new ClassScheduleProposalDTO(current.integer, state.getCombination()));
                if (result.size() == numberOfSolutions) return result;
            }
            for (ClassScheduleAlgoState successor : state.successors()) {
                int collisionCount = countOverlaps.applyAsInt(successor.getCombination());
                fringe.add(IntObjPair.of(collisionCount, successor));
            }
        }
        return result;
    }
}
