
package com.uth.BE.Repository;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;

import com.uth.BE.Entity.User;
import com.uth.BE.dto.req.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findBySeller(Optional<User> seller);
    List<Product> findBySeller(User seller);
    List<Product> findByCategory(Category category);
    List<Product> findByTitleContaining(String title);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    @Query("SELECT new com.uth.BE.dto.req.ProductDTO(p.product_id, p.title, p.description, p.price, p.status, null,p.createdAt) " +
            "FROM Product p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<ProductDTO> findProductsByKeyword(@Param("keyword") String keyword);

    @Query("SELECT new com.uth.BE.dto.req.ProductDTO(p.product_id, p.title, p.description, p.price, p.status,null,p.createdAt) " +
            "FROM Product p")
    List<ProductDTO> findAllProducts();

    @Query("SELECT p FROM Product p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);



}

