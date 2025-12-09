package com.telusko.SpringEcom.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private GoogleAuthFilter googleAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //disabling csrf request
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // <—— this enables @CrossOrigin to work
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/signup", "/api/user/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(googleAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
