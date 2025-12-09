package com.telusko.SpringEcom.service;


import com.telusko.SpringEcom.model.User;
import com.telusko.SpringEcom.model.dto.UserLoginRequest;
import com.telusko.SpringEcom.model.dto.UserLoginResponse;
import com.telusko.SpringEcom.model.dto.UserRegistrationRequest;
import com.telusko.SpringEcom.model.dto.UserRegistrationResponse;
import com.telusko.SpringEcom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    JwtService jwtService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserRegistrationResponse RegisterThisUser(UserRegistrationRequest req) {
        UserRegistrationResponse res=new UserRegistrationResponse();
        System.out.println(req.name()+" "+req.email()+" "+req.password()+" "+req.repassword());
        if(req.name().isBlank()||req.email().isBlank()||req.password().isBlank()||req.repassword().isBlank()){
            return null;
        }
        if(!req.password().equals(req.repassword())){
            return null;
        }
        User existingUser = userRepo.findByEmail(req.email());
        if(existingUser!=null){
            return null;
        }
        User user = new User();
        user.setEmailId(req.email());
        user.setName(req.name());
        user.setPassword(encoder.encode(req.password()));
        user.setUserId("userId"+UUID.randomUUID().toString().substring(0,16));
        System.out.println("Here is encoded password: "+user.getPassword());
        User savedUser = userRepo.save(user);
        return res;
    }

    public UserLoginResponse LoginThisUser(UserLoginRequest req) {

        if(req.email().isBlank()||req.password().isBlank()){
            return null;
        }

        User user=userRepo.findByEmail(req.email());

        if(user==null)return null;

        boolean passwordMatch = encoder.matches(req.password(), user.getPassword());
        if(!passwordMatch){
            return null;
        }
        String token=jwtService.generateToken(user.getEmailId());
        if(token==null)return null;

        UserLoginResponse res = new UserLoginResponse(
                user.getName(),
                token
        );
        return res;
    }
}

