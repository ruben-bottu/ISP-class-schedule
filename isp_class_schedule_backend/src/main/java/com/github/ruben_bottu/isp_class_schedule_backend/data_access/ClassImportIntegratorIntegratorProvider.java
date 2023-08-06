package com.github.ruben_bottu.isp_class_schedule_backend.data_access;

import com.github.ruben_bottu.isp_class_schedule_backend.data_access.external.ClassImportIntegrator;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.class_.ClassSummary;
import com.github.ruben_bottu.isp_class_schedule_backend.domain.course.Course;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.List;

public class ClassImportIntegratorIntegratorProvider implements IntegratorProvider {

    @Override
    public List<Integrator> getIntegrators() {
        return List.of(
                new ClassImportIntegrator(
                        List.of(
                                Course.class,
                                ClassSummary.class
                        )
                )
        );
    }
}
