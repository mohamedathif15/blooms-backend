package com.blooms.dto;

import com.blooms.model.CartItem;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String imageUrl;
    private BigDecimal pricePerStem;
    private BigDecimal lineTotal;
    private Integer quantity;
    private Integer minOrderQuantity;

    public static CartItemResponse from(CartItem c) {
        return CartItemResponse.builder()
                .id(c.getId())
                .productId(c.getProduct().getId())
                .productName(c.getProduct().getName())
                .imageUrl(c.getProduct().getImageUrl())
                .pricePerStem(c.getProduct().getPricePerStem())
                .quantity(c.getQuantity())
                .minOrderQuantity(
                        c.getProduct().getMinOrderQuantity())
                .lineTotal(c.getLineTotal())
                .build();
    }
}