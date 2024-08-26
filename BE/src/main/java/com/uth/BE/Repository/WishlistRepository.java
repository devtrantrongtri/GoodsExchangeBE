/*
package com.uth.BE.Repository;

import com.uth.BE.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findByUserUserId(int userId);
    @Override
    Optional<WishList> findById(Integer id);
    @Override
    void deleteById(Integer id);

    void deleteByUserUserIdAndProductsProductId(int userId, int product_id);
    Optional<WishList> findByUserUserIdAndProductsProductId(int userId, int product_id);
}
*/

package com.uth.BE.Repository;

import com.uth.BE.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findByUserUserId(int userId);

    @Override
    Optional<WishList> findById(Integer id);

    @Override
    void deleteById(Integer id);

    void deleteByUserUserIdAndProductsProductId(int userId, int product_id); // Sử dụng product_id
    Optional<WishList> findByUserUserIdAndProductsProductId(int userId, int product_id); // Sử dụng product_id
}
