package com.blooms.repository;

import com.blooms.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByIsActiveTrueOrderByIsFeaturedDescCreatedAtDesc();

    List<Product> findByCategoryIdAndIsActiveTrueOrderByCreatedAtDesc(
            Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND (" +
            "LOWER(p.name) LIKE LOWER(CONCAT('%',:kw,'%')) OR " +
            "LOWER(p.color) LIKE LOWER(CONCAT('%',:kw,'%')) OR " +
            "LOWER(p.originCountry) LIKE LOWER(CONCAT('%',:kw,'%')))")
    List<Product> searchProducts(@Param("kw") String keyword);

    long countByIsActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
            "AND p.stockQuantity < :threshold")
    List<Product> findLowStock(@Param("threshold") int threshold);
}