package com.blooms.dto;

import lombok.*;

@Data @Builder
public class AuthResponse {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private String companyName;
    private String token;
}