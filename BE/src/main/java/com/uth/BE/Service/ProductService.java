package com.uth.BE.Service;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private List<Product> products = new ArrayList<>();

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
