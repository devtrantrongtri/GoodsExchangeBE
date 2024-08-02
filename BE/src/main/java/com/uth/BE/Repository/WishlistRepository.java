package com.uth.BE.Repository;

import com.uth.BE.Pojo.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
}
