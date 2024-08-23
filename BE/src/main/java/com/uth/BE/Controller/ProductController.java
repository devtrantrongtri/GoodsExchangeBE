package com.uth.BE.Controller;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.ICategoryService;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.dto.res.GlobalRes;
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
    public GlobalRes<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products != null && !products.isEmpty()) {
            return new GlobalRes<List<Product>>(HttpStatus.OK,"success",products);
        } else {
            return new GlobalRes<List<Product>>(HttpStatus.NO_CONTENT,"success");
        }
    }

    @GetMapping("find_product_by_id/{productID}")
    public GlobalRes<Optional<Product>> findProductById(@PathVariable int productID) {
        Optional<Product> existingProduct = productService.getProductById(productID);
        if (existingProduct.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "Product found", existingProduct);
        }
        return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", Optional.empty());
    }

    @PostMapping("/create_product")
    public ResponseEntity<String> createProduct(@RequestBody Map<String, Object> payload) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Object categoryIdObj = payload.get("category_id");
            if (!(categoryIdObj instanceof Integer)) {
                return new ResponseEntity<>("Invalid category_id format", HttpStatus.BAD_REQUEST);
            }
            Integer categoryId = (Integer) categoryIdObj;
            Optional<Category> categoryOptional = Optional.ofNullable(categoryService.findById(categoryId));

            Optional<User> sellerOptional = userService.findByUsername(username);

            if (sellerOptional.isPresent() && categoryOptional.isPresent()) {
                User seller = sellerOptional.get();
                Category category = categoryOptional.get();

                Product product = new Product();
                product.setSeller(seller);
                product.setCategory(category);

                Object titleObj = payload.get("title");
                if (titleObj instanceof String) {
                    product.setTitle((String) titleObj);
                } else {
                    return new ResponseEntity<>("Invalid title format", HttpStatus.BAD_REQUEST);
                }

                Object descriptionObj = payload.get("description");
                if (descriptionObj instanceof String) {
                    product.setDescription((String) descriptionObj);
                } else {
                    return new ResponseEntity<>("Invalid description format", HttpStatus.BAD_REQUEST);
                }

                Object priceObj = payload.get("price");
                if (priceObj instanceof Double || priceObj instanceof Integer) {
                    product.setPrice(BigDecimal.valueOf(((Number) priceObj).doubleValue()));
                } else {
                    return new ResponseEntity<>("Invalid price format", HttpStatus.BAD_REQUEST);
                }

                Object statusObj = payload.get("status");
                if (statusObj instanceof String) {
                    product.setStatus((String) statusObj);
                } else {
                    return new ResponseEntity<>("Invalid status format", HttpStatus.BAD_REQUEST);
                }

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
                    Object categoryIdObj = payload.get("category_id");
                    if (categoryIdObj instanceof Integer) {
                        Integer categoryId = (Integer) categoryIdObj;
                        Optional<Category> categoryOptional = Optional.ofNullable(categoryService.findById(categoryId));
                        if (categoryOptional.isPresent()) {
                            existingProduct.setCategory(categoryOptional.get());
                        } else {
                            return new ResponseEntity<>("Invalid category_id", HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        return new ResponseEntity<>("Invalid category_id format", HttpStatus.BAD_REQUEST);
                    }
                }

                Object titleObj = payload.get("title");
                if (titleObj instanceof String) {
                    existingProduct.setTitle((String) titleObj);
                } else if (titleObj != null) {
                    return new ResponseEntity<>("Invalid title format", HttpStatus.BAD_REQUEST);
                }

                Object descriptionObj = payload.get("description");
                if (descriptionObj instanceof String) {
                    existingProduct.setDescription((String) descriptionObj);
                } else if (descriptionObj != null) {
                    return new ResponseEntity<>("Invalid description format", HttpStatus.BAD_REQUEST);
                }

                Object priceObj = payload.get("price");
                if (priceObj instanceof Double) {
                    existingProduct.setPrice(BigDecimal.valueOf((Double) priceObj));
                } else if (priceObj instanceof Integer) {
                    existingProduct.setPrice(BigDecimal.valueOf((Integer) priceObj));
                } else if (priceObj != null) {
                    return new ResponseEntity<>("Invalid price format", HttpStatus.BAD_REQUEST);
                }

                Object statusObj = payload.get("status");
                if (statusObj instanceof String) {
                    existingProduct.setStatus((String) statusObj);
                } else if (statusObj != null) {
                    return new ResponseEntity<>("Invalid status format", HttpStatus.BAD_REQUEST);
                }

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
    public GlobalRes<String> deleteProduct(@PathVariable int id) {
        try {
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isPresent()) {
                productService.deleteProductById(id);
                return new GlobalRes<>(HttpStatus.OK, "Product deleted successfully");
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete product: " + e.getMessage());
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
    public GlobalRes<List<Product>> searchProductsByUser(@PathVariable("id") int userId) {
        try {
            // Tìm người dùng theo ID
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Tìm sản phẩm theo người dùng
                List<Product> products = productService.searchProductsByUser(Optional.of(user));
                if (!products.isEmpty()) {
                    return new GlobalRes<>(HttpStatus.OK, "Products found", products);
                } else {
                    return new GlobalRes<>(HttpStatus.NO_CONTENT, "No products found for the given user");
                }
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search products: " + e.getMessage());
        }
    }




    @GetMapping("find_product_by_category_id/{categoryId}")
    public GlobalRes<List<Product>> searchProductsByCategory(@PathVariable int categoryId) {
        try {
            Category category = categoryService.findById(categoryId);
            if (category != null) {
                List<Product> products = productService.searchProductsByCategory(category);
                if (!products.isEmpty()) {
                    return new GlobalRes<>(HttpStatus.OK, "Products found", products);
                } else {
                    return new GlobalRes<>(HttpStatus.NO_CONTENT, "No products found for the given category");
                }
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "Category not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search products: " + e.getMessage());
        }
    }


    @GetMapping("find_product_by_title/{title}")
    public GlobalRes<List<Product>> searchProductsByTitle(@PathVariable String title) {
        try {
            List<Product> products = productService.searchProductsByTitle(title);
            if (!products.isEmpty()) {
                return new GlobalRes<>(HttpStatus.OK, "Products found", products);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT, "No products found with the given title");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search products: " + e.getMessage());
        }
    }


    @GetMapping("find_product_by_price/")
    public GlobalRes<List<Product>> searchProductsByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        try {
            List<Product> products = productService.searchProductsByPrice(minPrice, maxPrice);
            if (!products.isEmpty()) {
                return new GlobalRes<>(HttpStatus.OK, "Products found", products);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT, "No products found in the given price range");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search products: " + e.getMessage());
        }
    }

}
