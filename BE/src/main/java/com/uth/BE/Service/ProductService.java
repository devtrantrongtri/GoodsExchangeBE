package com.uth.BE.Service;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.ProductImgRepository;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.dto.req.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductImgRepository productImgRepository) {
        this.productRepository = productRepository;
        this.productImgRepository = productImgRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error occurred while fetching all products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Product> getProductById(int id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching product by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void createProduct(Product product) {
        try {
            productRepository.save(product);
        } catch (Exception e) {
            System.err.println("Error occurred while creating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProductById(int id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while deleting product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            productRepository.save(product);
        } catch (Exception e) {
            System.err.println("Error occurred while updating product: " + e.getMessage());
        }
    }

    public void changeStatusProduct(Product product, String newStatus){
        try{
            product.setStatus(newStatus);
            updateProduct(product);
        } catch (Exception e) {
            System.err.println("Error occurred while changing status product: " + e.getMessage());
        }
    }

    @Override
    public List<Product> searchProductsByUser(Optional<User> user) {
        try {
            return productRepository.findBySeller(user);
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by user: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> searchProductsByCategory(Category category) {
        try {
            return productRepository.findByCategory(category); // Phương thức này cần được định nghĩa trong ProductRepository
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by category: " + e.getMessage());
            return null;
        }
    }

    public List<Product> searchProductsByTitle(String title) {
        try {
            return productRepository.findByTitleContaining(title);
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by title: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Product> searchProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        } catch (Exception e) {
            System.err.println("Error occurred while searching products by price: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Page<Product> getAllProductsWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties) {
        Sort.Direction directed = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, directed, properties);
        return productRepository.findAll(pageable);
    }

    @Override
    public List<ProductDTO> getProductsWithImage(String keyword) {
        List<ProductDTO> products = productRepository.findProductsByKeyword(keyword);
        for (ProductDTO product : products) {
            List<String> imageUrls = productImgRepository.findImageUrlsByProductId(product.getProductId());
            product.setImageUrls(imageUrls);
        }
        return products;
    }

    @Override
    public List<ProductDTO> getAllProductsWithImage() {
        List<ProductDTO> products = productRepository.findAllProducts();
        for (ProductDTO product : products) {
            List<String> imageUrls = productImgRepository.findImageUrlsByProductId(product.getProductId());
            product.setImageUrls(imageUrls);
        }
        return products;
    }
}
