package isp_class_schedule_backend.model;

import lombok.*;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClassGroupDTO {
    @EqualsAndHashCode.Include
    Long id;
    String name;
}
