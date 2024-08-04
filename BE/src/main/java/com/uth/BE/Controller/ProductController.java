package com.uth.BE.Controller;

import com.uth.BE.Entity.Product;
import com.uth.BE.Service.Interface.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {
    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products != null && !products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{productID}")
    public ResponseEntity<Optional<Product>> findProductByid(@PathVariable int productID) {
        Optional<Product> existingProduct = productService.getProductById(productID);
        if (existingProduct.isPresent()){
            return new ResponseEntity<>(existingProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>(Optional.empty(),HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product){
        try {
            productService.createProduct(product);
            return new ResponseEntity<>("Product created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product product) {
        try {
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isPresent()) {
                product.setProduct_id(existingProduct.get().getProduct_id()); // get all, get detail

                productService.updateProduct(product);
                return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update Product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
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
            return new ResponseEntity<>("Failed to delete product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
