package com.blooms.dto;

import com.blooms.model.Order;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class OrderResponse {
    private Long id;
    private String invoiceNumber;
    private String status;
    private String buyerName;
    private String buyerEmail;
    private String companyName;
    private BigDecimal subtotal;
    private BigDecimal gstAmount;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private String contactName;
    private String contactPhone;
    private String notes;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime createdAt;

    public static OrderResponse from(Order o) {
        return OrderResponse.builder()
                .id(o.getId())
                .invoiceNumber(o.getInvoiceNumber())
                .status(o.getStatus().name())
                .buyerName(o.getUser().getName())
                .buyerEmail(o.getUser().getEmail())
                .companyName(o.getUser().getCompanyName())
                .subtotal(o.getSubtotal())
                .gstAmount(o.getGstAmount())
                .totalAmount(o.getTotalAmount())
                .deliveryAddress(o.getDeliveryAddress())
                .contactName(o.getContactName())
                .contactPhone(o.getContactPhone())
                .notes(o.getNotes())
                .createdAt(o.getCreatedAt())
                .orderItems(o.getOrderItems() == null
                        ? List.of()
                        : o.getOrderItems().stream()
                        .map(OrderItemResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}