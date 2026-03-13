package com.blooms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank String name;
    @Email @NotBlank String email;
    @NotBlank @Size(min = 6) String password;
    String phone;
    String companyName;
    String gstNumber;
    String deliveryAddress;
    String city;
    String state;
    String pincode;
}