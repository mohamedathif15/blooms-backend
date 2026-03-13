package com.blooms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull Long productId;
    @NotNull @Min(1) Integer quantity;
}