package com.uth.BE.Service.Interface;

import com.uth.BE.Pojo.ProductImg;

import java.util.List;
import java.util.Optional;

public interface IProductImgService {
    public Optional<ProductImg> getProductImgById(int id);
    public List<ProductImg> getAllProductImgs();
    public void createProductImg(ProductImg productImg);
    public void deleteProductImgById(int id);
    public void updateProductImg(ProductImg productImg);
}
