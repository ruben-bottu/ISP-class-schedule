package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm.Search;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.CourseRepository;
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
    private static ClassScheduleProperties properties;
    private static int defaultSolutionCount;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validatorValidation = factory.getValidator();
        properties = createClassScheduleProperties();
        // Shorthand for better readability
        defaultSolutionCount = properties.defaultSolutionCount();
    }

    private static ClassScheduleProperties createClassScheduleProperties() {
        return new ClassScheduleProperties(10, 20, 30);
    }

    private List<ClassScheduleProposal> getProposals(List<Long> courseIds, Integer solutionCount) {
        var contract = new ProposalsContract(courseIds, solutionCount, properties);
        return service.getProposals(contract);
    }

    // ######################
    // Proposals Course IDs #
    // ######################

    @Test
    public void givenNoCourseIds_whenGetProposalsIsCalled_thenEmptyListIsReturned() {
        var given = Collections.<Long>emptyList();
        var result = getProposals(given, defaultSolutionCount);
        assertThat(result).isEmpty();
    }

    @Test
    public void givenCourseIdsContainingNull_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = Arrays.asList(1L, null, 3L, 4L, 5L);
        var contract = new ProposalsContract(givenCourseIds, defaultSolutionCount, properties);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, defaultSolutionCount));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenMoreCourseIdsThanMaximum_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = LongStream.rangeClosed(0, properties.maxCourseIdsSize()).boxed().toList();
        var contract = new ProposalsContract(givenCourseIds, defaultSolutionCount, properties);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, defaultSolutionCount));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenDuplicateCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = Arrays.asList(81L, 7L, 9L, 4L, 4L);
        var contract = new ProposalsContract(givenCourseIds, defaultSolutionCount, properties);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, defaultSolutionCount));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenOneOrMoreNonexistentCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var givenCourseIds = Arrays.asList(2L, 5L, 7L, 11L, 3L, 4L);
        when(courseRepo.countByIdIn(givenCourseIds)).thenReturn(5);
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, defaultSolutionCount));
        assertThat(raisedException).isInstanceOf(NotFoundException.class)
                .extracting("resourceName")
                .isEqualTo("courseIds");
    }

    // ##########################
    // Proposals solution count #
    // ##########################

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
        var contract = new ProposalsContract(givenCourseIds, givenSolutionCount, properties);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(givenCourseIds, givenSolutionCount));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("solutionCount");
    }

    private ClassScheduleProposal buildProposal(int overlapCount) {
        return new ClassScheduleProposal(overlapCount, new ArrayList<>());
    }

    private List<ClassScheduleProposal> givenProposals(int expectedSolutionCount) {
        return IntStream.range(0, expectedSolutionCount).mapToObj(this::buildProposal).toList();
    }

    @Test
    public void givenNullSolutionCount_whenGetProposalsIsCalled_thenDefaultIsReturned() {
        var givenCourseIds = Arrays.asList(1L, 2L);
        Integer givenSolutionCount = null;
        var expectedSolutionCount = properties.defaultSolutionCount();

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(courseRepo.countByIdIn(givenCourseIds)).thenReturn(givenCourseIds.size());
        // In reality a list with the given Courses and their corresponding ClassGroups would be returned
        when(courseRepo.getCoursesWithClassGroups(givenCourseIds)).thenReturn(Collections.emptyList());
        when(search.greedySearch(any(), eq(expectedSolutionCount), any())).thenReturn(givenProposals(expectedSolutionCount));
        var result = getProposals(givenCourseIds, givenSolutionCount);

        assertThat(result).hasSize(expectedSolutionCount);
    }

    @Test
    public void givenSolutionCountBiggerThanMaximum_whenGetProposalsIsCalled_thenMaximumIsReturned() {
        var givenCourseIds = Arrays.asList(1L, 2L);
        var givenSolutionCount = properties.maxSolutionCount() + 1;
        var expectedSolutionCount = properties.maxSolutionCount();

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(courseRepo.countByIdIn(givenCourseIds)).thenReturn(givenCourseIds.size());
        // In reality a list with the given Courses and their corresponding ClassGroups would be returned
        when(courseRepo.getCoursesWithClassGroups(givenCourseIds)).thenReturn(Collections.emptyList());
        when(search.greedySearch(any(), eq(expectedSolutionCount), any())).thenReturn(givenProposals(expectedSolutionCount));
        var result = getProposals(givenCourseIds, givenSolutionCount);

        assertThat(result).hasSize(expectedSolutionCount);
    }
}
