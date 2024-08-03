package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    public Optional<Product> getProductById(int id);
    public List<Product> getAllProducts();
    public void createProduct(Product product);
    public void updateProduct(Product product);
    public void deleteProductById(int id);
}
