package com.turtleRun.be.controller;

import com.turtleRun.be.running.application.service.RunningApplicationService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/runningSession")
public class RunningSessionController {

    private RunningApplicationService runningApplicationService;
    @GetMapping("healthCheck")
    public String runningSessionHealthCheck(String testFlag) {
        return "runningSession is ok with : " + testFlag + "!!";
    }
}
