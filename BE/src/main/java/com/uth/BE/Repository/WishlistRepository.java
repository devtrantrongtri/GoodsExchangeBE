package com.uth.BE.Repository;

import com.uth.BE.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
}
