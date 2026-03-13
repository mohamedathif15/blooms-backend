package com.blooms.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @Builder
public class AdminStatsResponse {
    private long totalProducts;
    private long totalOrders;
    private long pendingOrders;
    private long totalBuyers;
    private long lowStockProducts;
    private BigDecimal totalRevenue;
}