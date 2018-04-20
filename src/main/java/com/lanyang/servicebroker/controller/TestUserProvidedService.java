package com.lanyang.servicebroker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ly on 3/7/18.
 */
@RestController
@RequestMapping("/user")
public class TestUserProvidedService {

    @RequestMapping("/providedService")
    public String hello(){
        return "hello";
    }
}
