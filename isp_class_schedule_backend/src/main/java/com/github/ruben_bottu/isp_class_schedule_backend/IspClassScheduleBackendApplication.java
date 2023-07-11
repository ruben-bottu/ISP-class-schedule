package com.github.ruben_bottu.isp_class_schedule_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.github.ruben_bottu.isp_class_schedule_backend")
public class IspClassScheduleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IspClassScheduleBackendApplication.class, args);
    }

}
