package com.blooms.service;

import com.blooms.dto.*;
import com.blooms.model.*;
import com.blooms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orders;
    private final CartItemRepository cart;
    private final ProductRepository products;
    private final UserRepository users;

    @Transactional
    public OrderResponse placeOrder(String email, CreateOrderRequest req) {

        var user = users.findByEmail(email).orElseThrow();
        var cartItems = cart.findByUserId(user.getId());

        if (cartItems.isEmpty())
            throw new RuntimeException("Cart is empty");

        for (CartItem ci : cartItems) {
            Product prod = ci.getProduct();
            if (prod.getStockQuantity() < ci.getQuantity())
                throw new RuntimeException(
                        "Insufficient stock: " + prod.getName() +
                                " (available: " + prod.getStockQuantity() + ")");
        }

        BigDecimal subtotal = cartItems.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal gst = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(gst);

        List<OrderItem> items = cartItems.stream().map(ci -> {
            Product prod = ci.getProduct();
            return OrderItem.builder()
                    .product(prod)
                    .quantity(ci.getQuantity())
                    .priceAtPurchase(prod.getPricePerStem())
                    .lineTotal(ci.getLineTotal())
                    .build();
        }).collect(Collectors.toList());

        String invoice = "BL-"
                + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%04d",
                (int)(Math.random() * 9000) + 1000);

        var order = orders.save(Order.builder()
                .user(user)
                .subtotal(subtotal)
                .gstAmount(gst)
                .totalAmount(total)
                .status(Order.OrderStatus.PENDING)
                .invoiceNumber(invoice)
                .deliveryAddress(req.getDeliveryAddress() != null
                        ? req.getDeliveryAddress()
                        : user.getDeliveryAddress())
                .contactName(req.getContactName() != null
                        ? req.getContactName()
                        : user.getName())
                .contactPhone(req.getContactPhone() != null
                        ? req.getContactPhone()
                        : user.getPhone())
                .notes(req.getNotes())
                .build());

        items.forEach(i -> i.setOrder(order));
        order.setOrderItems(items);
        orders.save(order);

        cart.deleteByUserId(user.getId());

        return OrderResponse.from(order);
    }

    public List<OrderResponse> getMyOrders(String email) {
        var user = users.findByEmail(email).orElseThrow();
        return orders.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrders() {
        return orders.findAllByOrderByCreatedAtDesc()
                .stream().map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateStatus(Long id, String status) {
        var order = orders.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        String oldStatus = order.getStatus().name();
        order.setStatus(Order.OrderStatus.valueOf(status));
        orders.save(order);

        if ("SHIPPED".equals(status) && !"SHIPPED".equals(oldStatus)) {
            for (OrderItem item : order.getOrderItems()) {
                Product prod = item.getProduct();
                int newStock = prod.getStockQuantity() - item.getQuantity();
                prod.setStockQuantity(Math.max(0, newStock));
                products.save(prod);
            }
        }

        return OrderResponse.from(orders.save(order));
    }

    public AdminStatsResponse getStats() {
        return AdminStatsResponse.builder()
                .totalProducts(products.countByIsActiveTrue())
                .totalOrders(orders.count())
                .pendingOrders(orders.countPendingOrders())
                .totalBuyers(orders.countDistinctBuyers())
                .totalRevenue(orders.getTotalRevenue())
                .lowStockProducts(products.findLowStock(200).size())
                .build();
    }
}
