package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import com.github.ruben_bottu.isp_class_schedule_backend.model.*;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.Course;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.courses.CourseRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.Lesson;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.LessonDTO;
import com.github.ruben_bottu.isp_class_schedule_backend.model.lessons.LessonRepository;
import com.github.ruben_bottu.isp_class_schedule_backend.service.ClassScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ClassScheduleRestController {

    private static final int DEFAULT_RESULT_LIMIT = 20;
    //private static final String STRING_DEFAULT_RESULT_LIMIT = Integer.toString(DEFAULT_RESULT_LIMIT);

    @Autowired
    private ClassScheduleService service;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private LessonRepository lessonRepo;

    @GetMapping("/class_schedule_sql")
    public String getCombinationsWithCollisionCountJson(@RequestParam(name = "limit", defaultValue = DEFAULT_RESULT_LIMIT+"") int resultLimit, @RequestBody List<Long> courseIds) {
        return lessonRepo.getCombinationsWithCollisionCountJson(resultLimit, courseIds);
    }

    @GetMapping("/overview")
    public List<String> overview() {
        return Arrays.asList("blazz", "foobar");
    }

    @GetMapping("/courses")
    public Iterable<Course> allCourses() {
        return service.getAllCourses();
    }

    @GetMapping("/courses_minimal")
    public Iterable<Course> allCoursesMinimal() {
        return courseRepo.findAll(Sort.unsorted());
    }

    @GetMapping("/lessons")
    public Iterable<Lesson> allLessons() {
        return lessonRepo.findAll(Sort.unsorted());
    }

    /*@GetMapping("/lessonIDSelect")
    public List<Lesson> allLessonWithSelect() {
        return lessonRepo.getLessons();
    }*/

    @GetMapping("/lessonIDSelect")
    public List<Lesson> allLessonWithSelect() {
        return service.getLessons();
    }

    @GetMapping("/lessonDTOs")
    public List<LessonDTO> allLessonDTOs() {
        return List.of(
                new LessonDTO(5L, LocalDateTime.of(2011, 8, 5, 15, 0), LocalDateTime.of(2011, 8, 5, 18, 0), "TestCourse", "TestClassGroup"),
                new LessonDTO(11L, LocalDateTime.of(2022, 1, 17, 9, 45), LocalDateTime.of(2023, 2, 18, 11, 15), "TestCourse", "TestClassGroup")
        );
    }



    @GetMapping("/lessonIDs")
    public List<?> allLessonIDs() {
        return lessonRepo.getLessonIDs();
    }

    /*@GetMapping("/bla")
    public List<String> testMethod() {
        return List.of(courseRepo.testMethod(List.of(new Course(1, "Algor"), new Course(2, "OOP"), new Course(3, "ZOP"), new Course(4, "bruh"))));
    }*/

    /*@GetMapping("/bla")
    public List<String> testMethod() {
        return List.of(courseRepo.testMethod(List.of( "Algor", "OOP", "ZOP", "bruh")));
    }*/

    @GetMapping("/size")
    public Long size() {
        return lessonRepo.count();
    }

    /*@GetMapping("/bla")
    public List<Integer> testMethod() {
        return List.of(lessonRepo.testMethod(List.of(
                new Object[]{1, 3},
                new Object[]{3, 3},
                new Object[]{4, 4},
                new Object[]{5, 4})));
    }*/

    /*@GetMapping("/bla")
    public int testMethod() {
        return lessonRepo.testMethod(List.of(
                new int[]{1, 3},
                new int[]{3, 3},
                new int[]{4, 4},
                new int[]{5, 4}));
    }*/

    @GetMapping("/old_count_collisions")
    public int oldCountCollisions() {
        return lessonRepo.oldCountCollisions(List.of(
                new CourseIdClassGroupId(1L, 3L),
                new CourseIdClassGroupId(3L, 3L),
                new CourseIdClassGroupId(4L, 4L),
                new CourseIdClassGroupId(5L, 4L)));
    }

    @GetMapping("/sizeOfGiven")
    public int sizeOfGiven() {
        //return lessonRepo.countRows("(1,2), (3,1), (4,4)");
        List<CourseIdClassGroupId> lst = List.of(
                new CourseIdClassGroupId(1L, 2L),
                new CourseIdClassGroupId(3L, 1L),
                new CourseIdClassGroupId(4L, 4L),
                new CourseIdClassGroupId(8L, 1L));
        String joinedIds = lst.stream()
                .map(CourseIdClassGroupId::toString)
                .collect(Collectors.joining(","));
        return lessonRepo.countRows(joinedIds);
    }

    @GetMapping("/count_collisions")
    public Integer countCollisions() {
        List<CourseIdClassGroupId> lst = List.of(
                new CourseIdClassGroupId(1L, 3L),
                new CourseIdClassGroupId(3L, 3L),
                new CourseIdClassGroupId(4L, 4L),
                new CourseIdClassGroupId(5L, 4L));
        String joinedIds = lst.stream()
                .map(CourseIdClassGroupId::toString)
                .collect(Collectors.joining(","));
        return lessonRepo.countCollisions(joinedIds);
    }

    /*@GetMapping("/variadic_test")
    public String varTest() {
        List<Integer> ints = Arrays.asList(9, 7, 6, 1);
        return lessonRepo.variadicTest(25, ints);
    }*/

    /*@GetMapping("/class_schedule/{row_limit}")
    public String getCombinationsWithCollisionCountJson(@PathVariable("row_limit") int rowLimit, @RequestBody List<Integer> courseIds) {
        //List<Integer> courseIds = Arrays.asList(1, 2, 3, 4, 5);
        return lessonRepo.getCombinationsWithCollisionCountJson(rowLimit, courseIds);
    }*/

    @GetMapping("/int_list")
    public List<Integer> intList(@RequestBody List<Integer> ints) {
        return ints;
    }

    // TODO
    /*@GetMapping("/array_test")
    public List<Integer> arrayTest() {
        List<Integer> result = lessonRepo.arrayTest();
        int bla = 5;
        System.out.println(bla);
        return result;
    }*/



    /*@GetMapping("/bla")
    public List<Integer> testMethod() {
        return List.of(lessonRepo.testMethod(List.of(
                new Object[]{1, 3},
                new Object[]{3, 3},
                new Object[]{4, 4},
                new Object[]{5, 4})));
    }*/

    // TODO defaultValue works, specifying limit in url doesn't work
    @GetMapping("class_schedule")
    public List<ClassScheduleProposalDTO> getClassScheduleProposals(@RequestParam(name = "limit", defaultValue = DEFAULT_RESULT_LIMIT+"") int numberOfSolutions, @RequestBody List<Long> courseIds) {
        /*int numberOfSolutions = 20;
        List<Long> courseIds = List.of(1L, 3L, 4L, 5L);*/
        System.out.println("number of solutions: " + numberOfSolutions);
        return service.getClassScheduleProposals(numberOfSolutions, courseIds);
    }

    @GetMapping("/coursesWithClassGroups")
    public List<Pair<CourseDTO, List<ClassGroupDTO>>> getCoursesWithClassGroups() {
        List<Long> courseIds = List.of(1L, 3L, 4L, 5L);
        return courseRepo.getCoursesWithClassGroups(courseIds);
    }

    @GetMapping("/count_overlaps")
    public int countOverlaps() { // TODO For the service: don't forget if (size == 1) return 0;
        // (1,2),(2,1),(3,1),(5,4)
        var course1 = new CourseDTO(1L, "");
        var classGroup1 = new ClassGroupDTO(2L, "");
        var course2 = new CourseDTO(2L, "");
        var classGroup2 = new ClassGroupDTO(1L, "");
        var course3 = new CourseDTO(3L, "");
        var classGroup3 = new ClassGroupDTO(1L, "");
        var course4 = new CourseDTO(5L, "");
        var classGroup4 = new ClassGroupDTO(4L, "");

        var pair1 = new CourseAndClassGroupDTO(course1, classGroup1);
        var pair2 = new CourseAndClassGroupDTO(course2, classGroup2);
        var pair3 = new CourseAndClassGroupDTO(course3, classGroup3);
        var pair4 = new CourseAndClassGroupDTO(course4, classGroup4);

        List<CourseAndClassGroupDTO> list = List.of(pair1, pair2, pair3, pair4);

        return lessonRepo.countOverlaps(list);
    }
}
