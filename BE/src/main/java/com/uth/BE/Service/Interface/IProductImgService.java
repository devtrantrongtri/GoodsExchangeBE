package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.ProductImg;

import java.util.List;
import java.util.Optional;

public interface IProductImgService {

    public ProductImg save(ProductImg productImg);

    public List<ProductImg> findAll();

    public Optional<ProductImg> findById(int id);

    public void delete(int id);

    public ProductImg update(ProductImg productImg);
}
