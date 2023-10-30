package com.github.ruben_bottu.class_scheduler_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.github.ruben_bottu.class_scheduler_backend")
public class ClassSchedulerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassSchedulerBackendApplication.class, args);
    }

}
