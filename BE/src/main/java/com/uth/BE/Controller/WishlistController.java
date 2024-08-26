package com.uth.BE.Controller;

import com.uth.BE.Entity.WishList;
import com.uth.BE.Entity.Product;
import com.uth.BE.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/addWishlist")
    public WishList addWishlist(@RequestBody WishList wishList) {
        return wishlistService.save(wishList);
    }

    @PostMapping("/addProduct")
    public WishList addProduct(@RequestBody Product product, @RequestParam int wishlistId) {
        Optional<WishList> wishList = wishlistService.findById(wishlistId);
        return wishList.map(wl -> wishlistService.addProduct(product, wl)).orElse(null);
    }

    @GetMapping("/findWishlistById/{id}")
    public Optional<WishList> findWishlistById(@PathVariable int id) {
        return wishlistService.findById(id);
    }

    @DeleteMapping("/deleteWishlist/{id}")
    public void deleteWishlist(@PathVariable int id) {
        wishlistService.delete(id);
    }
}
