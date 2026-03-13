package com.blooms.dto;

import com.blooms.model.OrderItem;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder
public class OrderItemResponse {
    private Long productId;
    private String productName;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal lineTotal;

    public static OrderItemResponse from(OrderItem i) {
        return OrderItemResponse.builder()
                .productId(i.getProduct().getId())
                .productName(i.getProduct().getName())
                .imageUrl(i.getProduct().getImageUrl())
                .quantity(i.getQuantity())
                .priceAtPurchase(i.getPriceAtPurchase())
                .lineTotal(i.getLineTotal())
                .build();
    }
}