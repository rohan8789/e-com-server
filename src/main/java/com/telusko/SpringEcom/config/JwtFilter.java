package com.telusko.SpringEcom.config;

import com.telusko.SpringEcom.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Get header sent from frontend
        String authHeader = request.getHeader("Authorization");
        String token=null;
        String userName=null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
        }
        //will run only if you dont have authentication, only for authentication purpose
        System.out.println("Next filter started : "+token);
        //for authorization, it will execute else
        if(token!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            if(jwtService.validateToken(token)){
                System.out.println("After validation:");
                //This is only to tell spring user is auth, let it pass
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        "dummyuser",null, Collections.emptyList()
                );
                auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}
