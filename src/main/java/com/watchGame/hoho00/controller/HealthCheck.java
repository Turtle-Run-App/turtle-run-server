package com.watchGame.hoho00.controller;


import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class HealthCheck {
    @GetMapping("healthCheck")
    public String healthCheck() {
        return "success!!!!";
    }
}
