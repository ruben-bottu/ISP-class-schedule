package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.course.CourseEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.ClassEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm.Search;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm.State;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.class_.ClassRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.validation.ProposalsContract;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.data.domain.Sort;

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

    public Iterable<CourseEntity> getAllCourses() {
        return courseRepo.findAll(Sort.unsorted());
    }

    public Iterable<ClassEntity> getAllClasses() {
        return classRepo.findAll(Sort.unsorted());
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

    private int countOverlaps(List<CourseGroup> list) {
        if (list.size() <= 1) return 0;
        return classRepo.countOverlaps(list);
    }

    private boolean allCourseIdsExist(List<Long> courseIds) {
        int foundCourseIdsCount = courseRepo.countByIdIn(courseIds);
        return courseIds.size() == foundCourseIdsCount;
    }

    /*public List<ClassScheduleProposalDTO> getProposals(List<Long> courseIds, int solutionCount) {
        if (solutionCount < 0) throw new IllegalArgumentException("Number of solutions cannot be negative, given: " + solutionCount);
        if (courseIds.isEmpty() || solutionCount == 0) return Collections.emptyList();
        if (listContainsDuplicates(courseIds) || !allCourseIdsExist(courseIds)) throw invalidCourseIdsException();
        int numberOfSolutions = Math.min(solutionCount, MAX_NUMBER_OF_SOLUTIONS);

        var coursesWithClassGroups = courseRepo.getCoursesWithClassGroups(courseIds);
        var algoState = new SearchAlgorithmState(coursesWithClassGroups);
        return Search.greedySearch(algoState, numberOfSolutions, this::countOverlaps);
    }*/

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
    }
}
