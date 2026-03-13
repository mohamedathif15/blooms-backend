package com.blooms.controller;

import com.blooms.dto.*;
import com.blooms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation
        .AuthenticationPrincipal;
import org.springframework.security.core.userdetails
        .UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService svc;

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> place(
            @AuthenticationPrincipal UserDetails u,
            @RequestBody CreateOrderRequest r) {
        return ResponseEntity.ok(
                svc.placeOrder(u.getUsername(), r));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> mine(
            @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(
                svc.getMyOrders(u.getUsername()));
    }
}