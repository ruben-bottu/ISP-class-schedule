package com.github.ruben_bottu.isp_class_schedule_backend.domain;

import com.github.ruben_bottu.isp_class_schedule_backend.domain.algorithm.Search;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course_group.CourseGroupRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.validation.MaxSizeConstraintValidator;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.validation.ProposalsContract;
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
    private CourseGroupRepository courseGroupRepo;
    @Mock
    private Validator validator;
    @Mock
    private Search search;
    @InjectMocks
    private ClassScheduleService service;
    private static Validator validatorValidation;
    private static ClassScheduleProperties properties;

    @BeforeClass
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validatorValidation = factory.getValidator();
        properties = createClassScheduleProperties();
        MaxSizeConstraintValidator.setMaxSize(properties.maxCourseIdsSize());
    }

    private static ClassScheduleProperties createClassScheduleProperties() {
        return new ClassScheduleProperties(10, 20, 30);
    }

    private List<ClassScheduleProposal> getProposals(List<Long> courseIds, Integer solutionCount) {
        var contract = new ProposalsContract(courseIds, solutionCount, properties);
        return service.getProposals(contract);
    }

    private ClassScheduleProposal buildProposal(int overlapCount) {
        return new ClassScheduleProposal(overlapCount, new ArrayList<>());
    }

    private List<ClassScheduleProposal> validProposalsWithSize(int expectedSolutionCount) {
        return IntStream.range(0, expectedSolutionCount).mapToObj(this::buildProposal).toList();
    }

    @Test
    public void givenValidCourseIdsAndSolutionCount_whenGetProposalsIsCalled_thenProposalsAreReturned() {
        var validCourseIds = Arrays.asList(1L, 2L);
        var validSolutionCount = properties.defaultSolutionCount();

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(courseRepo.countByIdIn(validCourseIds)).thenReturn(validCourseIds.size());
        // In reality a list with the given Courses and their corresponding ClassGroups would be returned
        when(courseGroupRepo.getGroupedByCourseIn(validCourseIds)).thenReturn(Collections.emptyList());
        when(search.greedySearch(any(), eq(validSolutionCount), any())).thenReturn(validProposalsWithSize(validSolutionCount));
        var result = getProposals(validCourseIds, properties.defaultSolutionCount());

        assertThat(result).hasSize(validSolutionCount);
    }


    // #####################
    // Proposal Course IDs #
    // #####################

    @Test
    public void givenNullAsCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        Exception raisedException = catchException(() -> getProposals(null, properties.defaultSolutionCount()));

        assertThat(raisedException).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void givenNoCourseIds_whenGetProposalsIsCalled_thenEmptyListIsReturned() {
        var noCourseIds = Collections.<Long>emptyList();

        var result = getProposals(noCourseIds, properties.defaultSolutionCount());

        assertThat(result).isEmpty();
    }

    @Test
    public void givenCourseIdsContainingNull_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var courseIdsContainingNull = Arrays.asList(1L, null, 3L, 4L, 5L);
        var contract = new ProposalsContract(courseIdsContainingNull, properties.defaultSolutionCount(), properties);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(courseIdsContainingNull, properties.defaultSolutionCount()));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenMoreCourseIdsThanMaximum_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var moreCourseIdsThanMax = LongStream.rangeClosed(0, properties.maxCourseIdsSize()).boxed().toList();
        var contract = new ProposalsContract(moreCourseIdsThanMax, properties.defaultSolutionCount(), properties);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(moreCourseIdsThanMax, properties.defaultSolutionCount()));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenDuplicateCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var duplicateCourseIds = Arrays.asList(81L, 7L, 9L, 4L, 4L);
        var contract = new ProposalsContract(duplicateCourseIds, properties.defaultSolutionCount(), properties);

        when(validator.validate(contract)).thenReturn(validatorValidation.validate(contract));
        Exception raisedException = catchException(() -> getProposals(duplicateCourseIds, properties.defaultSolutionCount()));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("courseIds");
    }

    @Test
    public void givenOneOrMoreNonexistentCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var courseIdsContainingNonexistent = Arrays.asList(2L, 5L, 7L, 11L, 3L, 4L);

        when(courseRepo.countByIdIn(courseIdsContainingNonexistent)).thenReturn(5);
        Exception raisedException = catchException(() -> getProposals(courseIdsContainingNonexistent, properties.defaultSolutionCount()));

        assertThat(raisedException).isInstanceOf(NotFoundException.class)
                .extracting("resourceName")
                .isEqualTo("courseIds");
    }


    // #########################
    // Proposal Solution Count #
    // #########################

    @Test
    public void givenNullSolutionCount_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var validCourseIds = Arrays.asList(1L, 2L);

        Exception raisedException = catchException(() -> getProposals(validCourseIds, null));

        assertThat(raisedException).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void givenSolutionCountSmallerThanMinusOne_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var validCourseIds = Arrays.asList(1L, 2L);
        var tooSmallSolutionCount = -2;
        var invalidContract = new ProposalsContract(validCourseIds, tooSmallSolutionCount, properties);

        when(validator.validate(invalidContract)).thenReturn(validatorValidation.validate(invalidContract));
        Exception raisedException = catchException(() -> getProposals(validCourseIds, tooSmallSolutionCount));

        assertThat(raisedException).isInstanceOf(ConstraintViolationException.class);
        var constraintViolations = ((ConstraintViolationException)raisedException).getConstraintViolations();
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations).extracting("propertyPath").asString().contains("solutionCount");
    }

    @Test
    public void givenSolutionCountOfMinusOne_whenGetProposalsIsCalled_thenDefaultIsReturned() {
        var validCourseIds = Arrays.asList(1L, 2L);
        var givenSolutionCount = -1;
        var expectedSolutionCount = properties.defaultSolutionCount();

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(courseRepo.countByIdIn(validCourseIds)).thenReturn(validCourseIds.size());
        // In reality a list with the given Courses and their corresponding ClassGroups would be returned
        when(courseGroupRepo.getGroupedByCourseIn(validCourseIds)).thenReturn(Collections.emptyList());
        when(search.greedySearch(any(), eq(expectedSolutionCount), any())).thenReturn(validProposalsWithSize(expectedSolutionCount));
        var result = getProposals(validCourseIds, givenSolutionCount);

        assertThat(result).hasSize(expectedSolutionCount);
    }

    @Test
    public void givenSolutionCountOfZero_whenGetProposalsIsCalled_thenEmptyListIsReturned() {
        var validCourseIds = Arrays.asList(1L, 2L);

        var result = getProposals(validCourseIds, 0);

        assertThat(result).isEmpty();
    }

    @Test
    public void givenSolutionCountBiggerThanMaximum_whenGetProposalsIsCalled_thenMaximumIsReturned() {
        var givenCourseIds = Arrays.asList(1L, 2L);
        var givenSolutionCount = properties.maxSolutionCount() + 1;
        var expectedSolutionCount = properties.maxSolutionCount();

        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(courseRepo.countByIdIn(givenCourseIds)).thenReturn(givenCourseIds.size());
        // In reality a list with the given Courses and their corresponding ClassGroups would be returned
        when(courseGroupRepo.getGroupedByCourseIn(givenCourseIds)).thenReturn(Collections.emptyList());
        when(search.greedySearch(any(), eq(expectedSolutionCount), any())).thenReturn(validProposalsWithSize(expectedSolutionCount));
        var result = getProposals(givenCourseIds, givenSolutionCount);

        assertThat(result).hasSize(expectedSolutionCount);
    }
}
