package com.blooms.controller;

import com.blooms.dto.ProductResponse;
import com.blooms.model.Category;
import com.blooms.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService svc;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> all(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(
                svc.getAll(search, categoryId));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> one(
            @PathVariable Long id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> categories() {
        return ResponseEntity.ok(svc.getAllCategories());
    }
}