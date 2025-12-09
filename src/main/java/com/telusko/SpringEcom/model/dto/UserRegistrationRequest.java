package com.telusko.SpringEcom.model.dto;

public record UserRegistrationRequest(
        String name,
        String email,
        String password,
        String repassword
) {
}
