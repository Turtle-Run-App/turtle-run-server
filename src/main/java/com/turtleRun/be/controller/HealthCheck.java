package com.turtleRun.be.controller;


import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/")
public class HealthCheck {
    @GetMapping("")
    public String healthCheck() {
        return "success!!!! in " + LocalDateTime.now();
    }
}
