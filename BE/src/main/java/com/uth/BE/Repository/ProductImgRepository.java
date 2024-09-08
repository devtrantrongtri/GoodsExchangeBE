package com.uth.BE.Repository;

import com.uth.BE.Entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    Optional<ProductImg> findByTitle(String title);
    @Query("SELECT pi.img_url FROM ProductImg pi WHERE pi.product.product_id = :productId")
    List<String> findImageUrlsByProductId(@Param("productId") int productId);
}
