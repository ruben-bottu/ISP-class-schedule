package com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm;

import java.util.Comparator;
import java.util.Random;

class FringeComparator implements Comparator<Fringe> {
    private static final FringeComparator INSTANCE = new FringeComparator();
    private static final Random RANDOM = new Random();

    private FringeComparator() {}

    public static Comparator<Fringe> getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(Fringe o1, Fringe o2) {
        // Prefer states with fewer overlaps
        int cmp = o1.overlapCount() - o2.overlapCount();
        if (cmp != 0) return cmp;

        // Prefer states deeper in the tree
        cmp = o2.state().getDepth() - o1.state().getDepth();
        if (cmp != 0) return cmp;

        // Randomly pick one
        return RANDOM.nextBoolean() ? 1 : -1;
    }
}
