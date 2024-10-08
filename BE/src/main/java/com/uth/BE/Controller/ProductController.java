package com.uth.BE.Controller;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.model.FileExtension;
import com.uth.BE.Service.DriveService;
import com.uth.BE.Service.Interface.ICategoryService;
import com.uth.BE.Service.Interface.IProductImgService;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.dto.req.PaginationRequest;
import com.uth.BE.dto.req.ProductDTO;
import com.uth.BE.dto.res.DriveDTO;
import com.uth.BE.dto.res.GlobalRes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final IProductService productService;
    private final IUserService userService;
    private final ICategoryService categoryService;
    private final IProductImgService productImgService;
    private final DriveService driveService;

    @Autowired
    public ProductController(IProductService productService, IUserService userService, ICategoryService categoryService, IProductImgService productImgService, DriveService driveService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.productImgService = productImgService;
        this.driveService = driveService;
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

    @PostMapping(value = "/create_product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalRes<String>> createProduct(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("category_id") Integer categoryId) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<Category> categoryOptional = Optional.ofNullable(categoryService.findById(categoryId));
            if (!categoryOptional.isPresent()) {
                return new ResponseEntity<>(new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid category_id"), HttpStatus.BAD_REQUEST);
            }

            Optional<User> sellerOptional = userService.findByUsername(username);
            if (!sellerOptional.isPresent()) {
                return new ResponseEntity<>(new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid seller"), HttpStatus.BAD_REQUEST);
            }

            Product product = new Product();
            product.setSeller(sellerOptional.get());
            product.setCategory(categoryOptional.get());
            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(BigDecimal.valueOf(price));
            product.setStatus("UNAVAILABLE");

            Product savedProduct = productService.createProduct(product);

            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String originalFilename = image.getOriginalFilename();
                    String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1) : "tmp";

                    DriveDTO driveDTO = driveService.uploadImgToDrive(image.getInputStream(), originalFilename);
                    if (driveDTO.getStatus() == 200) {
                        System.out.println("Drive URL: " + driveDTO.getUrl());

                        ProductImg productImg = new ProductImg();
                        productImg.setTitle("picture" + (images.indexOf(image) + 1));
                        productImg.setImgUrl(driveDTO.getUrl());
                        productImg.setFileExtension(FileExtension.valueOf(fileExtension.toUpperCase()));
                        productImg.setProduct(savedProduct);

                        productImgService.save(productImg);
                    } else {
                        return new ResponseEntity<>(new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image: " + driveDTO.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }

            return new ResponseEntity<>(new GlobalRes<>(HttpStatus.CREATED, "Product created successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create product: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update_product/{id}")
    public GlobalRes<String> updateProduct(@PathVariable int id, @RequestBody Map<String, Object> payload) {
        try {
            Optional<Product> existingProductOptional = productService.getProductById(id);
            if (!existingProductOptional.isPresent()) {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found");
            }
            Product existingProduct = existingProductOptional.get();
            int existingSellerId = existingProduct.getSeller().getUserId();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            Optional<User> currentUserOptional = userService.findByUsername(currentUsername);
            if (!currentUserOptional.isPresent()) {
                return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Current user not found");
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
                            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid category_id");
                        }
                    } else {
                        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid category_id format");
                    }
                }

                Object titleObj = payload.get("title");
                if (titleObj instanceof String) {
                    existingProduct.setTitle((String) titleObj);
                } else if (titleObj != null) {
                    return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid title format");
                }

                Object descriptionObj = payload.get("description");
                if (descriptionObj instanceof String) {
                    existingProduct.setDescription((String) descriptionObj);
                } else if (descriptionObj != null) {
                    return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid description format");
                }

                Object priceObj = payload.get("price");
                if (priceObj instanceof Double) {
                    existingProduct.setPrice(BigDecimal.valueOf((Double) priceObj));
                } else if (priceObj instanceof Integer) {
                    existingProduct.setPrice(BigDecimal.valueOf((Integer) priceObj));
                } else if (priceObj != null) {
                    return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid price format");
                }

                Object statusObj = payload.get("status");
                if (statusObj instanceof String) {
                    existingProduct.setStatus((String) statusObj);
                } else if (statusObj != null) {
                    return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Invalid status format");
                }

                productService.updateProduct(existingProduct);
                return new GlobalRes<>(HttpStatus.OK, "Product updated successfully");
            } else {
                return new GlobalRes<>(HttpStatus.FORBIDDEN, "Unauthorized: seller_id does not match");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update product: " + e.getMessage());
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

    @GetMapping("/getAllProductsWithPaginationAndSort")
    public GlobalRes<Page<Product>> getAllProductsWithPaginationAndSort(
            @Valid @RequestBody PaginationRequest request) {
        try {
            Page<Product> productsPage = productService.getAllProductsWithPaginationAndSort(
                    request.getOffset(), request.getPageSize(), request.getOrder(), request.getField());
            if (productsPage.hasContent()) {
                return new GlobalRes<>(HttpStatus.OK.value(), "success", productsPage);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT.value(), "No products found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST.value(), "Invalid parameters: " + e.getMessage());
        }
    }

    @GetMapping("/keyword/{keyword}")
    public GlobalRes<List<ProductDTO>> getProductsByKeyword(@PathVariable("keyword") String keyword) {
        List<ProductDTO> products = productService.getProductsWithImage(keyword);
        return new GlobalRes<>(HttpStatus.OK.value(), "success", products);
    }

    @GetMapping("/with-images")
    public GlobalRes<List<ProductDTO>> getAllProductsWithImages() {
        List<ProductDTO> products = productService.getAllProductsWithImage();
        return new GlobalRes<>(HttpStatus.OK.value(), "success", products);
    }

    @GetMapping("/getProductsWithPaginationAndSort/{keyword}")
    public GlobalRes<Page<ProductDTO>> getProductsByKeywordWithPaginationAndSort(
            @PathVariable("keyword") String keyword,
            @Valid @RequestBody PaginationRequest request) {
        try {
            Page<ProductDTO> productsPage = productService.getProductsByKeywordWithPaginationAndSort(
                    keyword, request.getOffset(), request.getPageSize(), request.getOrder(), request.getField());
            if (productsPage.hasContent()) {
                return new GlobalRes<>(HttpStatus.OK.value(), "success", productsPage);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT.value(), "No products found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST.value(), "Invalid parameter: " + e.getMessage());
        }
    }

    @PostMapping("/getAllProductsWithImagesWithSortAndPaging")
    public GlobalRes<Page<ProductDTO>> getAllProductsWithImagesWithSortAndPaging(
            @Valid @RequestBody PaginationRequest request) {
        try {
            Page<ProductDTO> productsPage = productService.getAllProductsWithImagesWithSortAndPaging(
                    request.getOffset(), request.getPageSize(), request.getOrder(), request.getField());
            if (productsPage.hasContent()) {
                return new GlobalRes<>(HttpStatus.OK.value(), "success", productsPage);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT.value(), "No products found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST.value(), "Invalid parameter: " + e.getMessage());
        }
    }


    @GetMapping("getProductDetail/{productID}")
    public GlobalRes<Optional<ProductDTO>> getProductDetail(@PathVariable int productID) {
        ProductDTO productDetail = productService.getProductDetail(productID);

        if (productDetail != null) {
            return new GlobalRes<>(HttpStatus.OK, "Product found", Optional.of(productDetail));
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", Optional.empty());
        }
    }
    @GetMapping("getAllProductByUsername/{username}")
    public GlobalRes<Optional<List<ProductDTO>>> getAllProductByUsername(@PathVariable String username) {
        List<ProductDTO> products = productService.getAllProductsByUsername(username);

        if (products != null) {
            return new GlobalRes<>(HttpStatus.OK, "Product found", Optional.of(products));
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", Optional.empty());
        }
    }
    @GetMapping("/countByCreate")
    public GlobalRes<Map<Timestamp, Long>> getCountProductsGroupedByCreatedAt() {
        try {
            Map<Timestamp, Long> counts = productService.countProductsGroupedByCreatedAt();
            if (!counts.isEmpty()) {
                return new GlobalRes<>(HttpStatus.OK, "Counts retrieved successfully", counts);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT, "No products found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve counts: " + e.getMessage());
        }
    }
}
