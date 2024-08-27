package com.uth.BE.Controller;

import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.WishList;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.ProductService;
import com.uth.BE.Service.WishlistService;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private final WishlistService wishlistService;
    private final IUserService userService;
    private final ProductService productService;

    public WishlistController(WishlistService wishlistService, IUserService userService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/addWishlist")
    public GlobalRes<String> createWishList(@RequestBody Map<String, Object> payload) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<User> userOptional = userService.findByUsername(username);

            Object productIdObj = payload.get("product_id");
            if (!(productIdObj instanceof Integer)) {
                return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid product_id format");
            }
            Integer productId = (Integer) productIdObj;

            Optional<Product> productOptional = productService.getProductById(productId);

            if (userOptional.isPresent() && productOptional.isPresent()) {
                User user = userOptional.get();
                Product product = productOptional.get();

                WishList wishList = new WishList();
                wishList.setUser(user);
                wishList.setProduct(product);

                wishlistService.createWishList(wishList);

                return new GlobalRes<>(HttpStatus.CREATED, "Wish list item created successfully");
            } else {
                return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid user_id or product_id");
            }

        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create wish list: " + e.getMessage());
        }
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
