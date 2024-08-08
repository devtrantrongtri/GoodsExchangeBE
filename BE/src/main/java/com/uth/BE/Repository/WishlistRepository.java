package com.uth.BE.Repository;

import com.uth.BE.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
//  List<WishList> findByUserUserId(int userId);
//
//    void deleteByUserUserId(int userId);
//
//    void deleteByUserUserIdAndProductProductId(int userId, int productId);
//
//    List<WishList> findByUserUserIdAndProductProductId(int userId, int productId);
}
