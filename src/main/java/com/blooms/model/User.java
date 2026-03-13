package com.blooms.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String companyName;
    private String gstNumber;

    @Column(columnDefinition = "TEXT")
    private String deliveryAddress;

    private String city;
    private String state;
    private String pincode;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.BUYER;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Role { BUYER, ADMIN }
}