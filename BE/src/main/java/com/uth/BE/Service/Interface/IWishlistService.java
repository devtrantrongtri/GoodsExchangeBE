package com.uth.BE.Service.Interface;

import com.uth.BE.Pojo.Product;
import com.uth.BE.Pojo.WishList;

import java.util.Optional;
import java.util.Set;

public interface IWishlistService {
    public WishList save(WishList wishList);

    public WishList addProduct(Product product, WishList wishList);

    public Set<Product> findAllProduct(WishList wishList);

    public Optional<WishList> findById(int id);

    public void delete(int id);
}
