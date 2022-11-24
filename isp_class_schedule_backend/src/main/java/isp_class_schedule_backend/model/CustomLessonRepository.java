package isp_class_schedule_backend.model;

import java.util.List;

public interface CustomLessonRepository {

    int countOverlaps(List<CourseAndClassGroupDTO> list);
}
