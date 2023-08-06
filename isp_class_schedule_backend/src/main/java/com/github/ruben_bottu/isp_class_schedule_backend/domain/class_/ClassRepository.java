package com.github.ruben_bottu.isp_class_schedule_backend.domain.class_;

import java.util.List;

public interface ClassRepository {

    List<ClassSummary> getByCourseGroupIdIn(List<Long> courseGroupIds);

    int countWeeksIn(List<Long> courseIds);

    String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds);

    int countOverlaps(List<Long> courseGroupIds);

    int countOverlapsBetween(Long courseGroupId, List<Long> courseGroupIds);
}
