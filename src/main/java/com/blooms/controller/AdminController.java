package com.blooms.controller;

import com.blooms.dto.*;
import com.blooms.model.*;
import com.blooms.repository.UserRepository;
import com.blooms.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost
        .PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class        AdminController {

    private final ProductService productSvc;
    private final OrderService orderSvc;
    private final UserRepository userRepo;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> stats() {
        return ResponseEntity.ok(orderSvc.getStats());
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> products() {
        return ResponseEntity.ok(productSvc.getAllAdmin());
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductRequest r) {
        return ResponseEntity.ok(productSvc.create(r));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest r) {
        return ResponseEntity.ok(
                productSvc.update(id, r));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        productSvc.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> categories() {
        return ResponseEntity.ok(
                productSvc.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCat(
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                productSvc.createCategory(
                        body.get("name"),
                        body.get("description")));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> orders() {
        return ResponseEntity.ok(orderSvc.getAllOrders());
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest r) {
        return ResponseEntity.ok(
                orderSvc.updateStatus(id, r.getStatus()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok(userRepo.findAll());
    }
}