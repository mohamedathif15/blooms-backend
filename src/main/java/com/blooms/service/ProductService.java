package com.blooms.service;

import com.blooms.dto.*;
import com.blooms.model.*;
import com.blooms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ProductService {

    private final ProductRepository products;
    private final CategoryRepository cats;

    public List<ProductResponse> getAll(
            String search, Long categoryId) {
        List<Product> list;
        if (search != null && !search.isBlank())
            list = products.searchProducts(search.trim());
        else if (categoryId != null)
            list = products
                    .findByCategoryIdAndIsActiveTrueOrderByCreatedAtDesc(
                            categoryId);
        else
            list = products
                    .findByIsActiveTrueOrderByIsFeaturedDescCreatedAtDesc();
        return list.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(Long id) {
        return products.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
    }

    public List<ProductResponse> getAllAdmin() {
        return products.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse create(ProductRequest r) {
        var cat = r.getCategoryId() != null
                ? cats.findById(r.getCategoryId()).orElse(null)
                : null;
        var p = Product.builder()
                .name(r.getName())
                .description(r.getDescription())
                .pricePerStem(r.getPricePerStem())
                .minOrderQuantity(r.getMinOrderQuantity() != null
                        ? r.getMinOrderQuantity() : 50)
                .stockQuantity(r.getStockQuantity() != null
                        ? r.getStockQuantity() : 0)
                .imageUrl(r.getImageUrl())
                .color(r.getColor())
                .originCountry(r.getOriginCountry())
                .stemLengthCm(r.getStemLengthCm())
                .season(r.getSeason())
                .vaseLifeDays(r.getVaseLifeDays())
                .isFeatured(Boolean.TRUE.equals(r.getIsFeatured()))
                .isActive(true)
                .category(cat)
                .build();
        return ProductResponse.from(products.save(p));
    }

    public ProductResponse update(Long id, ProductRequest r) {
        var p = products.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
        var cat = r.getCategoryId() != null
                ? cats.findById(r.getCategoryId())
                .orElse(p.getCategory())
                : p.getCategory();
        p.setName(r.getName());
        p.setDescription(r.getDescription());
        p.setPricePerStem(r.getPricePerStem());
        if (r.getMinOrderQuantity() != null)
            p.setMinOrderQuantity(r.getMinOrderQuantity());
        if (r.getStockQuantity() != null)
            p.setStockQuantity(r.getStockQuantity());
        if (r.getImageUrl() != null)
            p.setImageUrl(r.getImageUrl());
        if (r.getColor() != null)
            p.setColor(r.getColor());
        if (r.getOriginCountry() != null)
            p.setOriginCountry(r.getOriginCountry());
        if (r.getStemLengthCm() != null)
            p.setStemLengthCm(r.getStemLengthCm());
        if (r.getSeason() != null)
            p.setSeason(r.getSeason());
        if (r.getVaseLifeDays() != null)
            p.setVaseLifeDays(r.getVaseLifeDays());
        if (r.getIsFeatured() != null)
            p.setIsFeatured(r.getIsFeatured());
        p.setCategory(cat);
        return ProductResponse.from(products.save(p));
    }

    public void delete(Long id) {
        var p = products.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
        p.setIsActive(false);
        products.save(p);
    }

    public List<Category> getAllCategories() {
        return cats.findAllByOrderByDisplayOrderAsc();
    }

    public Category createCategory(
            String name, String desc) {
        return cats.save(Category.builder()
                .name(name).description(desc).build());
    }
}