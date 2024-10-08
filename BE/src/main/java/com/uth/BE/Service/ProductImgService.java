package com.uth.BE.Service;

import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Repository.ProductImgRepository;
import com.uth.BE.Service.Interface.IProductImgService;
import com.uth.BE.Entity.model.FileExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.uth.BE.Entity.Product;
import org.springframework.util.ReflectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImgService implements IProductImgService {
    @Autowired
    private ProductImgRepository productImgRepo;

    @Autowired
    private StoreService storeService;

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


    @Override
    public String saveProductImage(MultipartFile file, Product product) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        boolean isSaved = storeService.saveFile(file, fileName);
        if (!isSaved) {
            throw new RuntimeException("Failed to save the file");
        }

        ProductImg productImg = new ProductImg();
        productImg.setTitle(fileName);
        productImg.setImgUrl(storeService.loadFile(fileName).getURI().getPath());
        productImg.setFileExtension(FileExtension.valueOf(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase()));
        productImg.setProduct(product);
        productImgRepo.save(productImg);

        return "File uploaded successfully: " + fileName;
    }

    @Override
    public byte[] getProductImage(String fileName) throws IOException {
        ProductImg productImg = productImgRepo.findByTitle(fileName)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Resource resource = storeService.loadFile(fileName);
        return Files.readAllBytes(resource.getFile().toPath());
    }

    public List<ProductImg> findProductImgWithSort(String field, String order) {
        if (ReflectionUtils.findField(ProductImg.class, field) == null) {
            throw new IllegalArgumentException("Invalid field name: " + field);
        }
        return productImgRepo.findAll(Sort.by(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, field));
    }

    public Page<ProductImg> findProductImgWithPage(int offset, int size) {
        return productImgRepo.findAll(PageRequest.of(offset, size));
    }

    @Override
    public List<String> getUrlsProduct(int productId) {
        return productImgRepo.findImageUrlsByProductId(productId);
    }

}
