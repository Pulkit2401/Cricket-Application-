package com.example.cricketApp.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public String healthCheck() throws Exception {

        return "App is running fine";

    }
}
