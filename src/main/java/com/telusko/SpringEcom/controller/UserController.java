package com.telusko.SpringEcom.controller;


import com.telusko.SpringEcom.model.User;
import com.telusko.SpringEcom.model.dto.UserLoginRequest;
import com.telusko.SpringEcom.model.dto.UserLoginResponse;
import com.telusko.SpringEcom.model.dto.UserRegistrationRequest;
import com.telusko.SpringEcom.model.dto.UserRegistrationResponse;
import com.telusko.SpringEcom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api")

public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<UserRegistrationResponse> RegisterUser(@RequestBody UserRegistrationRequest req){
        UserRegistrationResponse userRegistrationResponse = userService.RegisterThisUser(req);
         return new ResponseEntity<>(userRegistrationResponse, HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserLoginResponse> LoginUser(@RequestBody UserLoginRequest req){
        UserLoginResponse userLoginResponse = userService.LoginThisUser(req);
        if(userLoginResponse==null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }

    @PostMapping("/user/login/google")
    public ResponseEntity<String> LoginGoogleUser(@RequestHeader("Google-Token") String str){
        if(str==null)return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(str, HttpStatus.OK);
    }
}
