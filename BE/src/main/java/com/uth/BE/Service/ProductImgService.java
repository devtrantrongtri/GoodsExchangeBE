package com.uth.BE.Service;

import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Repository.ProductImgRepository;
import com.uth.BE.Service.Interface.IProductImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImgService implements IProductImgService {
    private final ProductImgRepository productImgRepository;

    @Autowired
    public ProductImgService(ProductImgRepository productImgRepository) {
        this.productImgRepository = productImgRepository;
    }

    @Override
    public List<ProductImg> getAllProductImgs() {
        try {
            return productImgRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error occurred while fetching all product images: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<ProductImg> getProductImgById(int id) {
        try {
            return productImgRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching product image by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void createProductImg(ProductImg productImg) {
        try {
            productImgRepository.save(productImg);
        } catch (Exception e) {
            System.err.println("Error occurred while creating product image: " + e.getMessage());
        }
    }

    @Override
    public void deleteProductImgById(int id) {
        try {
            productImgRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while deleting product image: " + e.getMessage());
        }
    }

    @Override
    public void updateProductImg(ProductImg productImg) {
        try {
            productImgRepository.save(productImg);
        } catch (Exception e) {
            System.err.println("Error occurred while updating product image: " + e.getMessage());
        }
    }
}
