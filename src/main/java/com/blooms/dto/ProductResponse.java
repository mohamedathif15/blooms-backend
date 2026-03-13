package com.blooms.dto;

import com.blooms.model.Product;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String color;
    private String originCountry;
    private String season;
    private BigDecimal pricePerStem;
    private Integer minOrderQuantity;
    private Integer stockQuantity;
    private Integer stemLengthCm;
    private Integer vaseLifeDays;
    private Boolean isFeatured;
    private Boolean isActive;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;

    public static ProductResponse from(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .imageUrl(p.getImageUrl())
                .color(p.getColor())
                .originCountry(p.getOriginCountry())
                .season(p.getSeason())
                .pricePerStem(p.getPricePerStem())
                .minOrderQuantity(p.getMinOrderQuantity())
                .stockQuantity(p.getStockQuantity())
                .stemLengthCm(p.getStemLengthCm())
                .vaseLifeDays(p.getVaseLifeDays())
                .isFeatured(p.getIsFeatured())
                .isActive(p.getIsActive())
                .createdAt(p.getCreatedAt())
                .categoryId(p.getCategory() != null
                        ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null
                        ? p.getCategory().getName() : null)
                .build();
    }
}