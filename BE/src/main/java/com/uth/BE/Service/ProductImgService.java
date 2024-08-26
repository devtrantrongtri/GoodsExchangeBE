package com.uth.BE.Service;

import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Repository.ProductImgRepository;
import com.uth.BE.Service.Interface.IProductImgService;
import com.uth.BE.Entity.model.FileExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImgService implements IProductImgService {
    @Autowired
    private ProductImgRepository productImgRepo;

    @Override
    public ProductImg save(ProductImg productImg) {
        return productImgRepo.save(productImg);
    }

    @Override
    public List<ProductImg> findAll() {
        return productImgRepo.findAll();
    }

    @Override
    public Optional<ProductImg> findById(int id) {
        return productImgRepo.findById(id);
    }

    @Override
    public void delete(int id) {
        productImgRepo.deleteById(id);
    }

    @Override
    public ProductImg update(ProductImg productImg) {
        Optional<ProductImg> existingImg = productImgRepo.findById(productImg.getId());
        if (existingImg.isPresent()) {
            ProductImg img = existingImg.get();
            img.setTitle(productImg.getTitle());
            img.setImgUrl(productImg.getImgUrl());
            img.setFileExtension(productImg.getFileExtension());
            return productImgRepo.save(img);
        }
        return null;
    }


    private final String uploadDir = "/path/to/your/localstore/";

    public String saveProductImage(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());

        ProductImg productImg = new ProductImg();
        productImg.setTitle(fileName);
        productImg.setImgUrl(filePath.toString());
        productImg.setFileExtension(FileExtension.valueOf(file.getContentType()));
        productImgRepo.save(productImg);

        return "File uploaded successfully: " + fileName;
    }

    public byte[] getProductImage(String fileName) throws IOException {
        ProductImg productImg = productImgRepo.findByTitle(fileName)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path filePath = Paths.get(productImg.getImgUrl());
        return Files.readAllBytes(filePath);
    }
}
