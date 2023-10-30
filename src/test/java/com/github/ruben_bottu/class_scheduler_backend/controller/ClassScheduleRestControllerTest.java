package com.github.ruben_bottu.class_scheduler_backend.controller;

import com.github.ruben_bottu.class_scheduler_backend.ClassScheduleConfigurationProperties;
import com.github.ruben_bottu.class_scheduler_backend.domain.ClassScheduleService;
import com.github.ruben_bottu.class_scheduler_backend.domain.course.Course;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://github.com/eugenp/tutorials/blob/master/spring-boot-modules/spring-boot-testing/src/test/java/com/baeldung/boot/testing/EmployeeRestControllerIntegrationTest.java
@RunWith(SpringRunner.class)
@WebMvcTest(ClassScheduleRestController.class)
@Import(AuthenticationBasicConfiguration.class)
public class ClassScheduleRestControllerTest {
    private static final String CLASS_SCHEDULE_PATH = "/api/class-schedule";
    private static final String COURSES_PATH = "/courses";
    private static final String PROPOSALS_PATH = "/proposals";
    @MockBean
    private ClassScheduleService service;
    // This field also needs to be mocked, even though it's not used in the tests
    @MockBean
    private ClassScheduleConfigurationProperties properties;
    @Autowired
    private MockMvc classScheduleRestController;
    private Course algo, web1, bop, data1, testing;

    @Before
    public void setUp() {
        algo = new Course(1L, "Algo");
        web1 = new Course(2L, "Web 1");
        bop = new Course(3L, "BOP");
        data1 = new Course(4L, "Data 1");
        testing = new Course(5L, "Testing");
    }

    // TODO use object mother pattern
    @Test
    public void givenCourses_whenGetRequestForAllCourses_thenJsonWithAllCoursesIsReturned() throws Exception {
        var given = Arrays.asList(algo, web1, bop, data1, testing);
        when(service.getAllCourses()).thenReturn(given);
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + COURSES_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(algo.name())))
                .andExpect(jsonPath("$[1].name", Is.is(web1.name())));
    }

    @Test
    public void givenNoCourseIds_whenGetRequestForProposals_thenStatus404NotFoundIsReturned() throws Exception {
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/"))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void givenInvalidCourseIds_whenGetRequestForProposals_thenStatus400BadRequestIsReturned() throws Exception {
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/zy1x,bc"))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidQueryParameter_whenGetRequestForProposals_thenJsonWithDefaultValueIsReturned() throws Exception {
        var validCourseIds = Stream.of(algo, web1, bop).map(Course::id).map(id -> Long.toString(id)).collect(Collectors.joining(","));
        var validCount = "5";
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/" + validCourseIds + "?limit=" + validCount))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void givenInvalidCount_whenGetRequestForProposals_thenStatus400BadRequestIsReturned() throws Exception {
        var validCourseIds = Stream.of(algo, web1, bop).map(Course::id).map(id -> Long.toString(id)).collect(Collectors.joining(","));
        var invalidCount = "sj1m5g";
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/" + validCourseIds + "?count=" + invalidCount))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
