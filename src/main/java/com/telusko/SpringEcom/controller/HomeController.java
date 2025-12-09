package com.telusko.SpringEcom.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/hello")
    String sayHello(){
        System.out.println("HelloWorld");
        return "HelloWorld";
    }
    @GetMapping("/about")
    String sayAbout(){
        System.out.println("HelloWorld");
        return "about";
    }
}
