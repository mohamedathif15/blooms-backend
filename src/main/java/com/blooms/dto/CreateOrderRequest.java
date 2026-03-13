package com.blooms.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String deliveryAddress;
    private String contactName;
    private String contactPhone;
    private String notes;
}