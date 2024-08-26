package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.io.IOException;

public interface IProductImgService {

    public ProductImg save(ProductImg productImg);

    public List<ProductImg> findAll();

    public Optional<ProductImg> findById(int id);

    public void delete(int id);

    public ProductImg update(ProductImg productImg);

    public String saveProductImage(MultipartFile file, Product product) throws IOException;

    public byte[] getProductImage(String fileName) throws IOException;

}
