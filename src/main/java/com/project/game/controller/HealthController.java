package com.project.game.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by havyapanchal on 24 Apr, 2020 , 7:18 PM
 */
@RestController
public class HealthController {
    @GetMapping("/ping")
    public String ping() {
        return "Hurray! Server is running like flash!!!!" ;
    }
}
