package com.uth.BE.Controller;

import com.uth.BE.Entity.WishList;
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
    public WishList addWishlist(@RequestParam Integer productId, @RequestParam Integer userId) {
        return wishlistService.addProduct(productId, userId);
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
