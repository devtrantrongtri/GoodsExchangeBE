package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    public Optional<Product> getProductById(int id);
    public List<Product> getAllProducts();
    public void createProduct(Product product);
    public void updateProduct(Product product);
    public void deleteProductById(int id);
    public void changeStatusProduct(Product product, String status);
    List<Product> searchProductsByUser(Optional<User> user);
    List<Product> searchProductsByCategory(Category category);
}
