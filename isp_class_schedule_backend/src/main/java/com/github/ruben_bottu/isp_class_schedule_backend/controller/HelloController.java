package com.github.ruben_bottu.isp_class_schedule_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping("/overview")
    public List<String> overview() {
        return Arrays.asList("blazz", "foobar");
    }
}
