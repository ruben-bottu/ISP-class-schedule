package com.github.ruben_bottu.class_scheduler_backend.domain;

import com.github.ruben_bottu.class_scheduler_backend.domain.algorithm.Search;
import com.github.ruben_bottu.class_scheduler_backend.domain.algorithm.State;
import com.github.ruben_bottu.class_scheduler_backend.domain.class_.ClassRepository;
import com.github.ruben_bottu.class_scheduler_backend.domain.class_.ClassSummary;
import com.github.ruben_bottu.class_scheduler_backend.domain.course.Course;
import com.github.ruben_bottu.class_scheduler_backend.domain.course.CourseRepository;
import com.github.ruben_bottu.class_scheduler_backend.domain.course_group.CourseGroup;
import com.github.ruben_bottu.class_scheduler_backend.domain.course_group.CourseGroupRepository;
import com.github.ruben_bottu.class_scheduler_backend.domain.validation.IdListContract;
import com.github.ruben_bottu.class_scheduler_backend.domain.validation.ProposalsContract;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;

public class ClassScheduleService {
    private final CourseRepository courseRepo;
    private final CourseGroupRepository courseGroupRepo;
    private final ClassRepository classRepo;
    private final Validator validator;
    private final Search search;

    public ClassScheduleService(CourseRepository courseRepo, CourseGroupRepository courseGroupRepo, ClassRepository classRepo, Validator validator, Search search) {
        this.courseRepo = courseRepo;
        this.courseGroupRepo = courseGroupRepo;
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

    public String searchTreeToString() {
        var courseGroupsGroupedByCourse = courseGroupRepo.getGroupedByCourseIn(List.of(1L, 101L, 151L, 201L));
        courseGroupsGroupedByCourse.sort((lst1, lst2) -> (int) (lst1.get(0).course().id() - lst2.get(0).course().id()));
        var root = new TreeNode("empty", new ArrayList<>());
        var fringe = new ArrayDeque<TreeNode>();
        fringe.add(root);

        while (!fringe.isEmpty()) {
            var current = fringe.remove();
            int depth = (int) current.name().chars().filter(ch -> ch == '(').count();
            if (depth == courseGroupsGroupedByCourse.size()) continue;
            var courseGroups = courseGroupsGroupedByCourse.get(depth);
            for (CourseGroup courseGroup : courseGroups) {
                String data = current.name() + " (" + courseGroup.course().id() + "," + courseGroup.group().id() + ")";
                var node = new TreeNode(data, new ArrayList<>());
                current.children().add(node);
                fringe.add(node);
            }
        }
        return root.toString();
    }

    private <C> void validateConstraintsOf(C contract, String exceptionMessage) {
        var violations = validator.validate(contract);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(exceptionMessage, violations);
        }
    }

    private boolean allIdsExist(List<Long> ids, ToIntFunction<List<Long>> countByIdIn) {
        int foundIdsCount = countByIdIn.applyAsInt(ids);
        return ids.size() == foundIdsCount;
    }

    private <C> void validate(List<Long> ids, C contract, String contractViolationMessage, ToIntFunction<List<Long>> countByIdIn, NotFoundException notFoundException) {
        validateConstraintsOf(contract, contractViolationMessage);
        if (!allIdsExist(ids, countByIdIn)) throw notFoundException;
    }

    public List<ClassSummary> getClassesByCourseGroupIdIn(IdListContract contract) {
        var courseGroupIds = contract.ids();

        if (courseGroupIds.isEmpty()) return Collections.emptyList();
        validate(courseGroupIds, contract, "Invalid course group IDs", courseGroupRepo::countByIdIn, new NotFoundException("courseGroupIds", "Course group IDs don't exist"));

        return classRepo.getByCourseGroupIdIn(courseGroupIds);
    }

    private int countOverlaps(List<CourseGroup> courseGroups) {
        if (courseGroups.size() <= 1) return 0;
        List<Long> courseGroupIds = courseGroups.stream().map(CourseGroup::id).toList();
        return classRepo.countOverlaps(courseGroupIds);
    }

    private int countOverlapsBetween(CourseGroup courseGroup, List<CourseGroup> courseGroups) {
        if (courseGroups.isEmpty()) return 0;
        List<Long> courseGroupIds = courseGroups.stream().map(CourseGroup::id).toList();
        return classRepo.countOverlapsBetween(courseGroup.id(), courseGroupIds);
    }

    public List<ClassScheduleProposal> getProposals(ProposalsContract contract) {
        var courseIds = contract.courseIds();
        var solutionCount = contract.solutionCount();

        if (courseIds.isEmpty() || solutionCount == 0) return Collections.emptyList();
        validate(courseIds, contract, "Invalid proposal arguments", courseRepo::countByIdIn, new NotFoundException("courseIds", "Course IDs don't exist"));

        var courseGroups = courseGroupRepo.getGroupedByCourseIn(courseIds);
        var algorithmState = new State(courseGroups);
        return search.greedySearch(algorithmState, solutionCount, this::countOverlaps);
        // return search.greedySearchMemory(algorithmState, solutionCount, this::countOverlapsBetween);
    }
}
