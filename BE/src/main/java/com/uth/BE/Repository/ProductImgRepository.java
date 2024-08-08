package com.uth.BE.Repository;

import com.uth.BE.Entity.ProductImg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductImgRepository extends JpaRepository<ProductImg, Integer> {

}
