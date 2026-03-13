package com.blooms.service;

import com.blooms.dto.*;
import com.blooms.model.CartItem;
import com.blooms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cart;
    private final ProductRepository products;
    private final UserRepository users;

    public List<CartItemResponse> getCart(String email) {
        var user = users.findByEmail(email).orElseThrow();
        return cart.findByUserId(user.getId()).stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());
    }

    public CartItemResponse addToCart(
            String email, CartItemRequest r) {
        var user = users.findByEmail(email).orElseThrow();
        var product = products.findById(r.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
        var existing = cart.findByUserIdAndProductId(
                user.getId(), product.getId());
        CartItem item = existing.map(c -> {
            c.setQuantity(c.getQuantity() + r.getQuantity());
            return c;
        }).orElse(CartItem.builder()
                .user(user).product(product)
                .quantity(r.getQuantity()).build());
        return CartItemResponse.from(cart.save(item));
    }

    public CartItemResponse updateQty(
            String email, Long itemId, Integer qty) {
        var user = users.findByEmail(email).orElseThrow();
        var item = cart.findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Not your cart item");
        item.setQuantity(qty);
        return CartItemResponse.from(cart.save(item));
    }

    @Transactional
    public void remove(String email, Long itemId) {
        var user = users.findByEmail(email).orElseThrow();
        var item = cart.findById(itemId)
                .orElseThrow(() ->
                        new RuntimeException("Item not found"));
        if (!item.getUser().getId().equals(user.getId()))
            throw new RuntimeException("Not your cart item");
        cart.delete(item);
    }
}