package com.github.ruben_bottu.isp_class_schedule_backend.model;

import java.util.Comparator;
import java.util.Random;

public class ClassScheduleFringeComparator implements Comparator<IntObjPair<ClassScheduleAlgoState>> {
    private static final ClassScheduleFringeComparator INSTANCE = new ClassScheduleFringeComparator();
    private static final Random RANDOM = new Random();

    private ClassScheduleFringeComparator() {}

    public static Comparator<IntObjPair<ClassScheduleAlgoState>> getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(IntObjPair<ClassScheduleAlgoState> o1, IntObjPair<ClassScheduleAlgoState> o2) {
        // Prefer states with fewer overlaps
        int cmp = o1.integer - o2.integer;
        if (cmp != 0) return cmp;

        // Prefer states deeper in the tree
        cmp = o2.object.getDepth() - o1.object.getDepth();
        if (cmp != 0) return cmp;

        // Randomly pick one
        return RANDOM.nextBoolean() ? 1 : -1;
    }
}
