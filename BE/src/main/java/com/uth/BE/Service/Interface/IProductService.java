package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.dto.req.ProductDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductService {
    public Optional<Product> getProductById(int id);
    public List<Product> getAllProducts();
    void createProduct(Product product);
    public void updateProduct(Product product);
    public void deleteProductById(int id);
    public void changeStatusProduct(Product product, String status);
    List<Product> searchProductsByUser(Optional<User> user);
    List<Product> searchProductsByCategory(Category category);
    List<Product> searchProductsByTitle(String title);
    List<Product> searchProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice);
    Page<Product> getAllProductsWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties);
    List<ProductDTO> getProductsWithImage(String keyword);
    List<ProductDTO> getAllProductsWithImage();
    Page<ProductDTO> getProductsByKeywordWithPaginationAndSort(String keyword, int pageNumber, int pageSize, String direction, String properties);
    Page<ProductDTO> getAllProductsWithImagesWithSortAndPaging(int pageNumber, int pageSize, String direction, String properties);
    ProductDTO getProductDetail(int productId);
    List<ProductDTO> getAllProductsByUsername(String username);
    Map<Timestamp, Long> countProductsGroupedByCreatedAt();
}
