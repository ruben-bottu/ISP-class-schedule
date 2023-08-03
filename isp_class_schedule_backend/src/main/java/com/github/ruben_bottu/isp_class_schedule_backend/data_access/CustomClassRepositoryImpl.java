package com.github.ruben_bottu.isp_class_schedule_backend.data_access;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomClassRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    private int toInt(Object o) {
        return ((Long) o).intValue();
    }
}
