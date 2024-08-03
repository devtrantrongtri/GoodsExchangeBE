package com.uth.BE.Controller;

import com.uth.BE.Entity.WishList;
import com.uth.BE.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/addWishlist")
    public WishList addWishlist(@RequestBody WishList wishList) {
        return wishlistService.save(wishList);
    }

//    @PostMapping("/addProduct/")
//    public WishList addProduct(@RequestBody WishList wishList, @RequestBody Product product) {
//        return wishlistService.addProduct(product, wishList);
//    }

    @GetMapping("/findWistlistByID/{id}")
    public Optional<WishList> findWistlistById(@PathVariable int id) {
        return wishlistService.findById(id);
    }

    @DeleteMapping("/deleteWishlist/{id}")
    public void deleteComment(@PathVariable int id) {
        wishlistService.delete(id);
    }
}
