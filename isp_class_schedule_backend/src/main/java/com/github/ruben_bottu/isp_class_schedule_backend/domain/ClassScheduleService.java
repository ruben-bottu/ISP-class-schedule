package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm.Search;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm.State;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.class_.ClassSummary;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.class_.ClassRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.validation.ProposalsContract;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Collections;
import java.util.List;

public class ClassScheduleService {
    private final CourseRepository courseRepo;
    private final ClassRepository classRepo;
    private final Validator validator;
    private final Search search;

    public ClassScheduleService(CourseRepository courseRepo, ClassRepository classRepo, Validator validator, Search search) {
        this.courseRepo = courseRepo;
        this.classRepo = classRepo;
        this.validator = validator;
        this.search = search;
    }

    public String getCombinationsWithCollisionCountJson(int rowLimit, List<Long> courseIds) {
        return classRepo.getCombinationsWithCollisionCountJson(rowLimit, courseIds);
    }

    public List<Course> getAllCourses() {
        return courseRepo.getAll();
    }

    public List<ClassSummary> getClassesByCourseGroupIdIn(List<Long> courseGroupIds) {
        return classRepo.getByCourseGroupIdIn(courseGroupIds);
    }

    /*public String searchTreeToString() {
        var coursesWithGroups = courseRepo.getCourseGroupsGroupedByCourse(List.of(1L,3L,4L,5L));
        coursesWithGroups.sort((o1, o2) -> (int) (o1.first.id() - o2.first.id()));
        var root = new TreeNode("empty", new ArrayList<>());
        var fringe = new ArrayDeque<TreeNode>();
        fringe.add(root);

        while (!fringe.isEmpty()) {
            var current = fringe.remove();
            int depth = (int) current.name().chars().filter(ch -> ch == '(').count();
            if (depth == coursesWithGroups.size()) continue;
            var courseWithGroups = coursesWithGroups.get(depth);
            for (Group group : courseWithGroups.second) {
                String data = current.name() + " (" + courseWithGroups.first.id() + "," + group.id() + ")";
                var node = new TreeNode(data, new ArrayList<>());
                current.children().add(node);
                fringe.add(node);
            }
        }
        return root.toString();
    }*/

    private int countOverlaps(List<CourseGroup> courseGroups) {
        if (courseGroups.size() <= 1) return 0;
        // return classRepo.countOverlaps(courseGroups);
        List<Long> courseGroupIds = courseGroups.stream().map(CourseGroup::id).toList();
        return classRepo.countOverlaps(courseGroupIds);
    }

    private int countOverlapsBetween(CourseGroup courseGroup, List<CourseGroup> courseGroups) {
        if (courseGroups.isEmpty()) return 0;
        List<Long> courseGroupIds = courseGroups.stream().map(CourseGroup::id).toList();
        return classRepo.countOverlapsBetween(courseGroup.id(), courseGroupIds);
    }

    private boolean allCourseIdsExist(List<Long> courseIds) {
        int foundCourseIdsCount = courseRepo.countByIdIn(courseIds);
        return courseIds.size() == foundCourseIdsCount;
    }

    private <C> void validateConstraintsOf(C contract, String exceptionMessage) {
        var violations = validator.validate(contract);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(exceptionMessage, violations);
        }
    }

    public List<ClassScheduleProposal> getProposals(ProposalsContract contract) {
        var courseIds = contract.courseIds();
        var solutionCount = contract.solutionCount();

        if (courseIds.isEmpty() || solutionCount == 0) return Collections.emptyList();
        validateConstraintsOf(contract, "ProposalsContract");
        if (!allCourseIdsExist(courseIds)) throw new NotFoundException("courseIds", "Invalid course IDs");

        var courseGroups = courseRepo.getCourseGroupsGroupedByCourse(courseIds);
        var algorithmState = new State(courseGroups);
        return search.greedySearch(algorithmState, solutionCount, this::countOverlaps);
        // return search.greedySearchMemory(algorithmState, solutionCount, this::countOverlapsBetween);
    }
}
