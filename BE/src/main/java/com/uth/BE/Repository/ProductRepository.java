
package com.uth.BE.Repository;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;

import com.uth.BE.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findBySeller(Optional<User> seller);
    List<Product> findByCategory(Category category);
    List<Product> findByTitleContaining(String title);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}

