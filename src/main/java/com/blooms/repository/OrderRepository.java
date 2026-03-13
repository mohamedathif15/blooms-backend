package com.blooms.repository;

import com.blooms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Order> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o " +
            "WHERE o.status NOT IN ('CANCELLED')")
    BigDecimal getTotalRevenue();

    @Query("SELECT COUNT(o) FROM Order o " +
            "WHERE o.status IN ('PENDING','CONFIRMED','PROCESSING')")
    long countPendingOrders();

    @Query("SELECT COUNT(DISTINCT o.user.id) FROM Order o")
    long countDistinctBuyers();
}