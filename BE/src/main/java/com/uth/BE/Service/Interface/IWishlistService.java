package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.WishList;

import java.util.Optional;

public interface IWishlistService {
    WishList save(WishList wishList);
    WishList addProduct(Integer productId, Integer userId);
    Optional<WishList> findById(int id);
    void delete(int id);
    void createWishList(WishList wishList);
}
