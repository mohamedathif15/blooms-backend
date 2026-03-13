package com.blooms.service;

import com.blooms.dto.*;
import com.blooms.model.User;
import com.blooms.repository.UserRepository;
import com.blooms.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails
        .UserDetailsService;
import org.springframework.security.crypto.password
        .PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder enc;
    private final AuthenticationManager authManager;
    private final JwtUtil jwt;
    private final UserDetailsService uds;

    public AuthResponse register(RegisterRequest r) {
        if (repo.existsByEmail(r.getEmail()))
            throw new RuntimeException(
                    "Email already registered");

        var user = User.builder()
                .name(r.getName())
                .email(r.getEmail())
                .password(enc.encode(r.getPassword()))
                .phone(r.getPhone())
                .companyName(r.getCompanyName())
                .gstNumber(r.getGstNumber())
                .deliveryAddress(r.getDeliveryAddress())
                .city(r.getCity())
                .state(r.getState())
                .pincode(r.getPincode())
                .role(User.Role.BUYER)
                .isActive(true)
                .build();

        repo.save(user);
        return buildResponse(user);
    }

    public AuthResponse login(LoginRequest r) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        r.getEmail(), r.getPassword()));
        var user = repo.findByEmail(r.getEmail())
                .orElseThrow();
        return buildResponse(user);
    }

    private AuthResponse buildResponse(User u) {
        var token = jwt.generateToken(
                uds.loadUserByUsername(u.getEmail()));
        return AuthResponse.builder()
                .userId(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole().name())
                .companyName(u.getCompanyName())
                .token(token)
                .build();
    }
}