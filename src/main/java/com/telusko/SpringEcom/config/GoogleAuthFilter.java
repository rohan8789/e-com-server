package com.telusko.SpringEcom.config;

//import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
//import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.telusko.SpringEcom.model.User;
import com.telusko.SpringEcom.repository.UserRepo;
import com.telusko.SpringEcom.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleAuthFilter extends OncePerRequestFilter {

    @Value("${google.clientId}")
    private String CLIENT_ID;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String googleToken = request.getHeader("Google-Token");

        if (googleToken != null &&SecurityContextHolder.getContext().getAuthentication() == null) {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = null;
            try {
                idToken = verifier.verify(googleToken);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                User user = userRepo.findByEmail(email);
                if (user == null) {
                    user = new User();
                    user.setEmailId(email);
                    user.setName(name);
                    user.setPassword(""); // no password for Google login
                    userRepo.save(user);
                }

                //create jwt for google login and set it to response header
                String jwtForGoogle = jwtService.generateToken(user.getEmailId());
                response.setHeader("Authorization", "Bearer " + jwtForGoogle);
//              System.out.println("it is set to header "+response.getHeader("Authorization"));

                // Authenticate this request immediately rather than waiting for next cycle
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken("dummyuser", null, Collections.emptyList());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

                // TODO: Create Spring Security Authentication object
                System.out.println("Verified email: " + email);
            } else {
                System.out.println("Invalid ID token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
