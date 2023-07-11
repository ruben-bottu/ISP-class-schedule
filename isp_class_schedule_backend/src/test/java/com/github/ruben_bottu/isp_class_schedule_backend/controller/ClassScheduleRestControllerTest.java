package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import com.github.ruben_bottu.isp_class_schedule_backend.ClassScheduleConfigurationProperties;
import com.github.ruben_bottu.isp_class_schedule_backend.data_access.CourseEntity;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassGroup;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.ClassScheduleService;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.CourseBuilder;
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
    @MockBean
    private ClassScheduleConfigurationProperties properties;
    @Autowired
    private MockMvc classScheduleRestController;
    private CourseEntity algo, web1, bop, data1, testing;
    private ClassGroup oneTi1, oneTi2, oneTi3, twoTi5;

    @Before
    public void setUp() {
        algo = CourseBuilder.with(1L, "Algo");
        web1 = CourseBuilder.with(2L, "Web 1");
        bop = CourseBuilder.with(3L, "BOP");
        data1 = CourseBuilder.with(4L, "Data 1");
        testing = CourseBuilder.with(5L, "Testing");

        oneTi1 = new ClassGroup(1L, "ME-1TI/1");
        oneTi2 = new ClassGroup(2L, "ME-1TI/2");
        oneTi3 = new ClassGroup(3L, "ME-1TI/3");
        twoTi5 = new ClassGroup(4L, "ME-2TI/5");
    }

    @Test
    public void givenCourses_whenGetRequestForAllCourses_thenJsonWithAllCoursesIsReturned() throws Exception {
        var given = Arrays.asList(algo, web1, bop, data1, testing);
        when(service.getAllCourses()).thenReturn(given);
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + COURSES_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(algo.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(web1.getName())));
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

    // The default value is used instead of the incorrect Query Parameter
    @Test
    public void givenInvalidQueryParameter_whenGetRequestForProposals_thenStatus200OkIsReturned() throws Exception {
        var validCourseIds = Stream.of(algo, web1, bop).map(CourseEntity::getId).map(id -> Long.toString(id)).collect(Collectors.joining(","));
        var validCount = "5";
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/" + validCourseIds + "?limit=" + validCount))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void givenInvalidCount_whenGetRequestForProposals_thenStatus400BadRequestIsReturned() throws Exception {
        var validCourseIds = Stream.of(algo, web1, bop).map(CourseEntity::getId).map(id -> Long.toString(id)).collect(Collectors.joining(","));
        var invalidCount = "sj1m5g";
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/" + validCourseIds + "?count=" + invalidCount))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
