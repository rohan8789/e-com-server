package com.telusko.SpringEcom.service;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class GoogleController {
    @GetMapping("/hello")
    String sayHello(){
        System.out.println("HelloWorld");
        return "HelloWorld";
    }
    @PostMapping("/auth/google")
    String sayAbout(@RequestBody String token){
        System.out.println(token );

        return "about";
    }
}
