package com.uth.BE.Repository;

import com.uth.BE.Entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {
    Optional<ProductImg> findByTitle(String title);
}
