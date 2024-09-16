package com.uth.BE.Controller;
import java.util.Collections;

import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.WishList;
import com.uth.BE.Entity.model.CustomUserDetails;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.Interface.IWishlistService;
import com.uth.BE.Service.ProductService;
import com.uth.BE.Service.WishlistService;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private final WishlistService wishlistService;
    private final IUserService userService;
    private final ProductService productService;
    private IWishlistService userRepository;

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

    public GlobalRes<String> deleteWishList(@PathVariable int id) {
        try {
            wishlistService.deleteWishListById(id);
            return new GlobalRes<>(HttpStatus.OK, "Wish list item deleted successfully");
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete wish list: " + e.getMessage());
        }
    }

    @GetMapping("/getAllWishlists")
    public ResponseEntity<List<WishList>> getAllWishlists() {
        List<WishList> wishLists = wishlistService.getAllWishlists();
        if (wishLists.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(wishLists, HttpStatus.OK);
    }


    @PutMapping("/updateWishlist/{wishListId}")
    public GlobalRes<String> updateWishlist(@PathVariable Integer wishListId, @RequestBody Map<String, Object> payload) {
        try {
            // Lấy productId từ payload
            Object productIdObj = payload.get("product_id");
            if (!(productIdObj instanceof Integer)) {
                return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid product_id format");
            }
            Integer newProductId = (Integer) productIdObj;

            // Cập nhật danh sách mong muốn
            wishlistService.updateWishList(wishListId, newProductId);

            return new GlobalRes<>(HttpStatus.OK, "Wishlist updated successfully");
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update wishlist: " + e.getMessage());
        }
    }

    @PostMapping("/addProductToWishList")
    public GlobalRes<String> addProductToWishList(@RequestBody Map<String, Integer> payload) {
        try {
            Integer userId = payload.get("user_id");
            Integer productId = payload.get("product_id");

            // Kiểm tra nếu userId và productId không bị null
            if (userId == null || productId == null) {
                return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Missing user_id or product_id");
            }

            wishlistService.addProductToWishList(userId, productId);
            return new GlobalRes<>(HttpStatus.OK, "Product added to wishlist successfully");
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add product to wishlist: " + e.getMessage());
        }
    }

    // Get products in wishlist
    @GetMapping("/{wishlistId}/products")
    public ResponseEntity<List<Product>> getProductsInWishlist(@PathVariable int wishlistId) {
        List<Product> products = wishlistService.getProductsInWishlist(wishlistId);
        return ResponseEntity.ok(products);
    }

    // Remove product from wishlist
    @DeleteMapping("/{wishlistId}/products/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(@PathVariable int wishlistId, @PathVariable int productId) {
        try {
            wishlistService.removeProductFromWishlist(wishlistId, productId);
            return ResponseEntity.ok("Product removed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wishlist or Product not found");
        }
    }

    // Get wishlist analytics
    @GetMapping("/{wishlistId}/analytics")
    public ResponseEntity<String> getWishlistAnalytics(@PathVariable int wishlistId) {
        String analytics = wishlistService.getWishlistAnalytics(wishlistId);
        return ResponseEntity.ok(analytics);
    }

    // Sort wishlist products
    @PostMapping("/{wishlistId}/sort")
    public ResponseEntity<List<Product>> sortWishlistProducts(@PathVariable int wishlistId) {
        try {
            // Gọi service để sắp xếp sản phẩm
            List<Product> sortedProducts = wishlistService.sortWishlistProducts(wishlistId);

            // Nếu danh sách đã sắp xếp là rỗng, trả về danh sách ban đầu và thông báo
            if (sortedProducts.isEmpty()) {
                System.out.println("No products found in the wishlist after sorting.");
                return ResponseEntity.ok(Collections.emptyList()); // Trả về danh sách trống với mã trạng thái 200
            }

            return ResponseEntity.ok(sortedProducts); // Trả về danh sách đã sắp xếp với mã trạng thái 200
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
    @GetMapping("/getTheirWishList")
    public ResponseEntity<List<WishList>> getWishListForUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
            Integer userId = customUserDetails.getUserId();
            Optional<User> user = userService.getUserById(userId);
            if (user.isPresent()) {
                User userObj = user.get();
                List<WishList> wishLists = wishlistService.getWishListByUser(userObj);
                return ResponseEntity.ok(wishLists);
            } else {
                return ResponseEntity.status(404).build();
            }

        } else {
            return ResponseEntity.status(403).build(); // Unauthorized
        }
    }
    @DeleteMapping("/deleteWishListByProduct/{productId}")
    public GlobalRes<String> deleteWishListByProduct(@PathVariable Integer productId) {
        try {
            // Lấy thông tin user từ security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Optional<User> userOptional = userService.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Kiểm tra xem product có tồn tại không
                Optional<Product> productOptional = productService.getProductById(productId);
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();

                    // Tìm wishlist của user với product_id tương ứng
                    Optional<WishList> wishListOptional = wishlistService.findByUserAndProduct(user, product);

                    if (wishListOptional.isPresent()) {
                        // Xóa wishlist item
                        wishlistService.deleteWishListById(wishListOptional.get().getId());
                        return new GlobalRes<>(HttpStatus.OK, "Wish list item deleted successfully");
                    } else {
                        return new GlobalRes<>(HttpStatus.NOT_FOUND, "Wish list item not found for this product");
                    }
                } else {
                    return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found");
                }
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete wish list: " + e.getMessage());
        }
    }

}


