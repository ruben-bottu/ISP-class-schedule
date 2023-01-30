package com.github.ruben_bottu.isp_class_schedule_backend.model;

import java.util.Comparator;
import java.util.Random;

public class SearchAlgorithmFringeComparator implements Comparator<SearchAlgorithmFringeDTO> {
    private static final SearchAlgorithmFringeComparator INSTANCE = new SearchAlgorithmFringeComparator();
    private static final Random RANDOM = new Random();

    private SearchAlgorithmFringeComparator() {}

    public static Comparator<SearchAlgorithmFringeDTO> getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(SearchAlgorithmFringeDTO o1, SearchAlgorithmFringeDTO o2) {
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
