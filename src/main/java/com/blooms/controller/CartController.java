package com.blooms.controller;

import com.blooms.dto.*;
import com.blooms.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation
        .AuthenticationPrincipal;
import org.springframework.security.core.userdetails
        .UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService svc;

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> get(
            @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(
                svc.getCart(u.getUsername()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> add(
            @AuthenticationPrincipal UserDetails u,
            @Valid @RequestBody CartItemRequest r) {
        return ResponseEntity.ok(
                svc.addToCart(u.getUsername(), r));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CartItemResponse> update(
            @AuthenticationPrincipal UserDetails u,
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
                svc.updateQty(u.getUsername(), id, quantity));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> remove(
            @AuthenticationPrincipal UserDetails u,
            @PathVariable Long id) {
        svc.remove(u.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}