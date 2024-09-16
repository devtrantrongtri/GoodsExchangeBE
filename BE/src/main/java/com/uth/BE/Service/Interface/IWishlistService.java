package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.WishList;

import java.util.List;
import java.util.Optional;

public interface IWishlistService {
    WishList save(WishList wishList);
    WishList addProduct(Integer productId, Integer userId);
    Optional<WishList> findById(int id);
    void delete(int id);
    void createWishList(WishList wishList);
    List<WishList> getWishListByUser(User userId) ;
    Optional<WishList> findByUserAndProduct(User user, Product product) ;
}
