package com.blooms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank String name;
    String description;
    String imageUrl;
    String color;
    String originCountry;
    String season;
    @NotNull BigDecimal pricePerStem;
    Integer minOrderQuantity;
    Integer stockQuantity;
    Integer stemLengthCm;
    Integer vaseLifeDays;
    Boolean isFeatured;
    Long categoryId;
}