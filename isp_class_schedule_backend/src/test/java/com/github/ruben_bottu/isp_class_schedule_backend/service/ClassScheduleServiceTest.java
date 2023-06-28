package com.github.ruben_bottu.isp_class_schedule_backend.service;

import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassScheduleProposalDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassScheduleService;
import com.github.ruben_bottu.isp_class_schedule_backend.model.NotFoundException;
import com.github.ruben_bottu.isp_class_schedule_backend.model.ProposalsContract;
import com.github.ruben_bottu.isp_class_schedule_backend.model.algorithm.Search;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseRepository;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static com.github.ruben_bottu.isp_class_schedule_backend.controller.ClassScheduleRestController.DEFAULT_SOLUTION_COUNT;
import static com.github.ruben_bottu.isp_class_schedule_backend.model.ProposalsContract.MAX_COURSE_IDS_SIZE;
import static com.github.ruben_bottu.isp_class_schedule_backend.model.ProposalsContract.MAX_SOLUTION_COUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClassScheduleServiceTest {

    @Mock
    private CourseRepository courseRepo;
    @Mock
    private Validator validator;
    @Mock
    private Search search;
    @InjectMocks
    private ClassScheduleService service;
    private static Validator validatorValidation;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validatorValidation = factory.getValidator();
    }

    private List<ClassScheduleProposalDTO> getProposals(List<Long> courseIds, int solutionCount) {
        var contract = new ProposalsContract(courseIds, solutionCount);
        return service.getProposals(contract);
    }

    @Test
    public void givenNoCourseIds_whenGetProposalsIsCalled_thenEmptyListIsReturned() {
        var given = Collections.<Long>emptyList();
        var result = getProposals(given, DEFAULT_SOLUTION_COUNT);
        assertThat(result).isEmpty();
    }

    @Test
    public void givenCourseIdsContainingNull_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = Arrays.asList(1L, null, 3L, 4L, 5L);
        var contract = new ProposalsContract(givenCourseIds, DEFAULT_SOLUTION_COUNT);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, DEFAULT_SOLUTION_COUNT));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenMoreCourseIdsThanMaximum_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = LongStream.rangeClosed(0, MAX_COURSE_IDS_SIZE).boxed().toList();
        var contract = new ProposalsContract(givenCourseIds, DEFAULT_SOLUTION_COUNT);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, DEFAULT_SOLUTION_COUNT));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenDuplicateCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = Arrays.asList(81L, 7L, 9L, 4L, 4L);
        var contract = new ProposalsContract(givenCourseIds, DEFAULT_SOLUTION_COUNT);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, DEFAULT_SOLUTION_COUNT));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenOneOrMoreNonexistentCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = Arrays.asList(2L, 5L, 7L, 11L, 3L, 4L);
        when(courseRepo.countByIdIn(givenCourseIds)).thenReturn(5);
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, DEFAULT_SOLUTION_COUNT));
        assertThat(raisedException).isInstanceOf(NotFoundException.class)
                .extracting("resourceName")
                .isEqualTo("courseIds");
    }

    @Test
    public void givenSolutionCountOfZero_whenGetProposalsIsCalled_thenEmptyListIsReturned() {
        var givenCourseIds = Arrays.asList(1L, 2L);
        var result = getProposals(givenCourseIds, 0);
        assertThat(result).isEmpty();
    }

    @Test
    public void givenNegativeSolutionCount_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = Arrays.asList(1L, 2L);
        var givenSolutionCount = -1;
        var contract = new ProposalsContract(givenCourseIds, givenSolutionCount);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, givenSolutionCount));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("solutionCount");
    }

    private ClassScheduleProposalDTO buildProposal(int overlapCount) {
        return new ClassScheduleProposalDTO(overlapCount, new ArrayList<>());
    }

    private List<ClassScheduleProposalDTO> givenProposals(int expectedSolutionCount) {
        return IntStream.range(0, expectedSolutionCount).mapToObj(this::buildProposal).toList();
    }

    @Test
    public void givenSolutionCountBiggerThanMaximum_whenGetProposalsIsCalled_thenMaximumIsReturned() {
        var givenCourseIds = Arrays.asList(1L, 2L);
        var givenSolutionCount = MAX_SOLUTION_COUNT + 1;
        var expectedSolutionCount = MAX_SOLUTION_COUNT;

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(courseRepo.countByIdIn(givenCourseIds)).thenReturn(givenCourseIds.size());
        // In reality a list with the given Courses and their corresponding ClassGroups would be returned
        when(courseRepo.getCoursesWithClassGroups(givenCourseIds)).thenReturn(Collections.emptyList());
        when(search.greedySearch(any(), eq(expectedSolutionCount), any())).thenReturn(givenProposals(expectedSolutionCount));
        var result = getProposals(givenCourseIds, givenSolutionCount);

        assertThat(result).hasSize(expectedSolutionCount);
    }
}
