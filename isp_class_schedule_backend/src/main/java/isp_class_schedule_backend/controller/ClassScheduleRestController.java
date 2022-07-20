package isp_class_schedule_backend.controller;

import isp_class_schedule_backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ClassScheduleRestController {

    private static final int DEFAULT_RESULT_LIMIT = 20;
    //private static final String STRING_DEFAULT_RESULT_LIMIT = Integer.toString(DEFAULT_RESULT_LIMIT);

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private LessonRepository lessonRepo;

    @GetMapping("/overview")
    public List<String> overview() {
        return Arrays.asList("blazz", "foobar");
    }

    @GetMapping("/courses")
    public Iterable<Course> allCourses() {
        return courseRepo.findAll();
    }

    @GetMapping("/lessons")
    public Iterable<Lesson> allLessons() {
        return lessonRepo.findAll();
    }

    @GetMapping("/lessonIDSelect")
    public List<Lesson> allLessonWithSelect() {
        return lessonRepo.getLessons();
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

    @GetMapping("/bla")
    public List<Integer> testMethod() {
        return List.of(lessonRepo.testMethod(List.of(
                new Object[]{1, 3},
                new Object[]{3, 3},
                new Object[]{4, 4},
                new Object[]{5, 4})));
    }

    @GetMapping("/old_count_collisions")
    public List<Integer> oldCountCollisions() {
        return List.of(lessonRepo.oldCountCollisions(List.of(
                new CourseIdClassGroupId(1, 3),
                new CourseIdClassGroupId(3, 3),
                new CourseIdClassGroupId(4, 4),
                new CourseIdClassGroupId(5, 4))));
    }

    @GetMapping("/sizeOfGiven")
    public int sizeOfGiven() {
        //return lessonRepo.countRows("(1,2), (3,1), (4,4)");
        List<CourseIdClassGroupId> lst = List.of(
                new CourseIdClassGroupId(1, 2),
                new CourseIdClassGroupId(3, 1),
                new CourseIdClassGroupId(4, 4),
                new CourseIdClassGroupId(8, 1));
        String joinedIds = lst.stream()
                .map(CourseIdClassGroupId::toString)
                .collect(Collectors.joining(","));
        return lessonRepo.countRows(joinedIds);
    }

    @GetMapping("/count_collisions")
    public Integer countCollisions() {
        List<CourseIdClassGroupId> lst = List.of(
                new CourseIdClassGroupId(1, 3),
                new CourseIdClassGroupId(3, 3),
                new CourseIdClassGroupId(4, 4),
                new CourseIdClassGroupId(5, 4));
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

    @GetMapping("/class_schedule")
    public String getCombinationsWithCollisionCountJson(@RequestParam(name = "limit", defaultValue = DEFAULT_RESULT_LIMIT+"") int resultLimit, @RequestBody List<Integer> courseIds) {
        return lessonRepo.getCombinationsWithCollisionCountJson(resultLimit, courseIds);
    }

    @GetMapping("/int_list")
    public List<Integer> intList(@RequestBody List<Integer> ints) {
        return ints;
    }
}
