package com.uth.BE.Controller;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.ICategoryService;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;
    private final IUserService userService;
    private final ICategoryService categoryService;

    @Autowired
    public ProductController(IProductService productService, IUserService userService, ICategoryService categoryService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/get_all_product")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products != null && !products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("find_product_by_id/{productID}")
    public ResponseEntity<Product> findProductById(@PathVariable int productID) {
        Optional<Product> existingProduct = productService.getProductById(productID);
        if (existingProduct.isPresent()) {
            return new ResponseEntity<>(existingProduct.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create_product")
    public ResponseEntity<String> createProduct(@RequestBody Map<String, Object> payload) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<User> sellerOptional = userService.findByUsername(username);
            Optional<Category> categoryOptional = Optional.ofNullable(categoryService.findById((Integer) payload.get("category_id")));

            if (sellerOptional.isPresent() && categoryOptional.isPresent()) {
                User seller = sellerOptional.get();
                Category category = categoryOptional.get();

                Product product = new Product();
                product.setSeller(seller);
                product.setCategory(category);
                product.setTitle((String) payload.get("title"));
                product.setDescription((String) payload.get("description"));

                Object priceObj = payload.get("price");
                if (priceObj instanceof Double || priceObj instanceof Integer) {
                    product.setPrice(BigDecimal.valueOf(((Number) priceObj).doubleValue()));
                } else {
                    return new ResponseEntity<>("Invalid price format", HttpStatus.BAD_REQUEST);
                }

                product.setStatus((String) payload.get("status"));

                productService.createProduct(product);
                return new ResponseEntity<>("Product created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Invalid category_id", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update_product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        try {
            Optional<Product> existingProductOptional = productService.getProductById(id);
            if (!existingProductOptional.isPresent()) {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
            Product existingProduct = existingProductOptional.get();
            int existingSellerId = existingProduct.getSeller().getUserId(); // Lấy seller_id của sản phẩm

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> currentUserOptional = userService.findByUsername(currentUsername);
            if (!currentUserOptional.isPresent()) {
                return new ResponseEntity<>("Current user not found", HttpStatus.UNAUTHORIZED);
            }

            User currentUser = currentUserOptional.get();
            int currentUserId = currentUser.getUserId();

            if (existingSellerId == currentUserId) {
                if (payload.containsKey("category_id")) {
                    Integer categoryId = (Integer) payload.get("category_id");
                    Optional<Category> categoryOptional = Optional.ofNullable(categoryService.findById(categoryId));
                    if (categoryOptional.isPresent()) {
                        existingProduct.setCategory(categoryOptional.get());
                    } else {
                        return new ResponseEntity<>("Invalid category_id", HttpStatus.BAD_REQUEST);
                    }
                }

                existingProduct.setTitle((String) payload.get("title"));
                existingProduct.setDescription((String) payload.get("description"));

                Object priceObj = payload.get("price");
                if (priceObj instanceof Double) {
                    existingProduct.setPrice(BigDecimal.valueOf((Double) priceObj));
                } else if (priceObj instanceof Integer) {
                    existingProduct.setPrice(BigDecimal.valueOf((Integer) priceObj));
                }

                existingProduct.setStatus((String) payload.get("status"));

                productService.updateProduct(existingProduct);
                return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Unauthorized: seller_id does not match", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @DeleteMapping("delete_product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        try {
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isPresent()) {
                productService.deleteProductById(id);
                return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable int id, @RequestParam String status) {
        try {
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();
                productService.changeStatusProduct(product, status);
                return new ResponseEntity<>("Product status updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("find_product_by_user_id/{id}")
    public ResponseEntity<List<Product>> searchProductsByUser(@PathVariable("id") int userId) {
        try {
            // Tìm người dùng theo ID
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Tìm sản phẩm theo người dùng
                List<Product> products = productService.searchProductsByUser(Optional.of(user));
                if (!products.isEmpty()) {
                    return new ResponseEntity<>(products, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("find_product_by_category_id/{categoryId}")
    public ResponseEntity<List<Product>> searchProductsByCategory(@PathVariable int categoryId) {
        try {
            Category category = categoryService.findById(categoryId);
            if (category != null) {
                List<Product> products = productService.searchProductsByCategory(category);
                if (!products.isEmpty()) {
                    return new ResponseEntity<>(products, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("find_product_by_title/{title}")
    public ResponseEntity<List<Product>> searchProductsByTitle(@PathVariable String title) {
        try {
            List<Product> products = productService.searchProductsByTitle(title);
            if (!products.isEmpty()) {
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("find_product_by_price/")
    public ResponseEntity<List<Product>> searchProductsByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        try {
            List<Product> products = productService.searchProductsByPrice(minPrice, maxPrice);
            if (!products.isEmpty()) {
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
