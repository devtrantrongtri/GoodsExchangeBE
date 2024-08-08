package com.uth.BE.Controller;

import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Service.ProductImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductImgController {

    @Autowired
    private ProductImgService productImgService;

    @PostMapping("/createProductImg")
    public ProductImg createProductImg(@RequestBody ProductImg productImg) {
        return productImgService.save(productImg);
    }

    @GetMapping("/readAllProductImg")
    public List<ProductImg> readAllProductImg() {
        return productImgService.findAll();
    }

    @GetMapping("/readProductImgByID/{id}")
    public Optional<ProductImg> readProductImgById(@PathVariable int id) {
        return productImgService.findById(id);
    }

    @DeleteMapping("/deleteProductImg/{id}")
    public void deleteProductImg(@PathVariable int id) {
        productImgService.delete(id);
    }

    @PostMapping("/updateProductImg")
    public ProductImg updateProductImg(@RequestBody ProductImg productImg) {
        return productImgService.update(productImg);
    }
}
