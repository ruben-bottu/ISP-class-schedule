package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import com.github.ruben_bottu.isp_class_schedule_backend.model.ClassGroupDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.CourseBuilder;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.service.ClassScheduleService;
import jakarta.servlet.ServletException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
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
    private static final String CLASS_SCHEDULE_PATH = "/api/schedule";
    private static final String COURSES_PATH = "/courses";
    private static final String PROPOSALS_PATH = "/proposals";
    @MockBean
    private ClassScheduleService service;
    @Autowired
    private MockMvc classScheduleRestController;
    private Course algo, web1, bop, data1, testing;
    private ClassGroupDTO oneTi1, oneTi2, oneTi3, twoTi5;

    @Before
    public void setUp() {
        algo = CourseBuilder.with(1L, "Algo");
        web1 = CourseBuilder.with(2L, "Web 1");
        bop = CourseBuilder.with(3L, "BOP");
        data1 = CourseBuilder.with(4L, "Data 1");
        testing = CourseBuilder.with(5L, "Testing");

        oneTi1 = new ClassGroupDTO(1L, "ME-1TI/1");
        oneTi2 = new ClassGroupDTO(2L, "ME-1TI/2");
        oneTi3 = new ClassGroupDTO(3L, "ME-1TI/3");
        twoTi5 = new ClassGroupDTO(4L, "ME-2TI/5");
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

    private void givenBadCourseIds_whenGetRequestForProposals_thenErrorIsThrown(String givenPath) throws Exception {
        Exception raisedException = catchException(() -> classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/" + givenPath)));
        assertThat(raisedException).isInstanceOf(ServletException.class)
                .hasMessageContaining("Invalid course IDs");
    }

    @Test
    public void givenInvalidCourseIds_whenGetRequestForProposals_thenErrorIsThrown() throws Exception {
        givenBadCourseIds_whenGetRequestForProposals_thenErrorIsThrown("zy1x+bc");
    }

    @Test
    public void givenDuplicateCourseIds_whenGetRequestForProposals_thenEmptyJsonListIsReturned() throws Exception {
        givenBadCourseIds_whenGetRequestForProposals_thenErrorIsThrown("1+2+3+5+1+8");
    }

    @Test
    public void givenInvalidLimit_whenGetRequestForProposals_thenStatus400BadRequestIsReturned() throws Exception {
        var validCourseIds = Stream.of(algo, web1, bop).map(Course::getId).map(id -> Long.toString(id)).collect(Collectors.joining("+"));
        var invalidLimit = "sj1m5g";
        classScheduleRestController.perform(get(CLASS_SCHEDULE_PATH + PROPOSALS_PATH + "/" + validCourseIds + "?limit=" + invalidLimit))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
