package com.blooms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequest {
    @Email @NotBlank String email;
    @NotBlank String password;
}