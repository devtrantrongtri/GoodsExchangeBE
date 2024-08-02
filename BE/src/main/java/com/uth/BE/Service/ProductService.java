package com.uth.BE.Service;

import com.uth.BE.Pojo.Product;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

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
            return null;
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
}
