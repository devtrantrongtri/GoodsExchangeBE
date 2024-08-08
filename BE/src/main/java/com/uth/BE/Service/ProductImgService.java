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

    @Autowired
    private ProductImgRepository repo;

    public ProductImgService(ProductImgRepository repo) {
        super();
        this.repo = repo;
    }

    @Override
    public ProductImg save(ProductImg productImg) {
        return repo.save(productImg);
    }

    @Override
    public List<ProductImg> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<ProductImg> findById(int id) {
        return repo.findById(id);
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }

    @Override
    public ProductImg update(ProductImg productImg) {
        ProductImg existing = repo.findById(productImg.getProductimgId()).orElse(null);
        if (existing != null) {
            existing.setImgUrl(productImg.getImgUrl());
            existing.setTitle(productImg.getTitle());
            existing.setFileExtension(productImg.getFileExtension());
            existing.setProduct(productImg.getProduct());
            return repo.save(existing);
        }
        return null;
    }
}
