package com.github.ruben_bottu.isp_class_schedule_backend.service;

import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.LessonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.ruben_bottu.isp_class_schedule_backend.controller.ClassScheduleRestController.DEFAULT_NUMBER_OF_SOLUTIONS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClassScheduleServiceTest {

    @Mock
    private CourseRepository courseRepo;
    @Mock
    private LessonRepository lessonRepo;
    @InjectMocks
    private ClassScheduleService service;

    @Test
    public void givenNoCourseIds_whenGetProposalsIsCalled_thenEmptyListIsReturned() {
        var given = Collections.<Long>emptyList();
        var result = service.getProposals(given, DEFAULT_NUMBER_OF_SOLUTIONS);
        assertThat(result).isEmpty();
    }

    private void givenBadCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown(List<Long> given) {
        Exception raisedException = catchException(() -> service.getProposals(given, DEFAULT_NUMBER_OF_SOLUTIONS));
        assertThat(raisedException).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid course IDs");
    }

    @Test
    public void givenDuplicateCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var given = Arrays.asList(81L, 7L, 9L, 4L, 4L);
        givenBadCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown(given);
    }

    @Test
    public void givenOneOrMoreCourseIdsThatDontExist_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var given = Arrays.asList(2L, 5L, 7L, 11L, 3L, 4L);
        when(courseRepo.countByIdIn(given)).thenReturn(5);
        givenBadCourseIds_whenGetProposalsIsCalled_thenErrorIsThrown(given);
    }

    @Test
    public void givenZeroRequestedNumberOfSolutions_whenGetProposalsIsCalled_thenEmptyListIsReturned() {
        var given = Arrays.asList(1L, 2L);
        var result = service.getProposals(given, 0);
        assertThat(result).isEmpty();
    }

    @Test
    public void givenNegativeRequestedNumberOfSolutions_whenGetProposalsIsCalled_thenErrorIsThrown() {
        var given = Arrays.asList(1L, 2L);
        Exception raisedException = catchException(() -> service.getProposals(given, -1));
        assertThat(raisedException).isInstanceOf(IllegalArgumentException.class);
    }
}
