package com.uth.BE.Controller;

import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Service.Interface.IProductImgService;
import com.uth.BE.dto.req.ProductImgReq;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import com.uth.BE.Entity.Product;
import com.uth.BE.Service.Interface.IProductService;



@RestController
@RequestMapping("/productimgs")
public class ProductImgController {
    @Autowired
    private IProductImgService productImgService;

    @Autowired
    private IProductService productService;

    @PostMapping()
    public GlobalRes<ProductImg> createProductImg(@RequestBody ProductImgReq productImgReq) {
        ProductImg img = new ProductImg(productImgReq.getTitle(), productImgReq.getImgUrl(), productImgReq.getFileExtension());

        Optional<Product> product = productService.getProductById(productImgReq.getProductId());

        img.setProduct(product.orElse(null));

        ProductImg savedImg = productImgService.save(img);

        if (savedImg != null) {
            return new GlobalRes<>(HttpStatus.CREATED, "Successfully created the product image", null);
        }

        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to create the product image", null);
    }


    @GetMapping()
    public GlobalRes<List<ProductImg>> getAllProductImgs() {
        List<ProductImg> imgs = productImgService.findAll();
        if (!imgs.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully retrieved all product images", imgs);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to retrieve product images", null);
    }

    @GetMapping("/{id}")
    public GlobalRes<ProductImg> getProductImgById(@PathVariable int id) {
        Optional<ProductImg> img = productImgService.findById(id);
        if (img.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully retrieved the product image", img.get());
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Product image not found", null);
    }

    @PutMapping("/{id}")
    public GlobalRes<ProductImg> updateProductImg(@PathVariable int id, @RequestBody ProductImgReq productImgReq) {
        Optional<ProductImg> imgOptional = productImgService.findById(id);
        if (imgOptional.isPresent()) {
            ProductImg img = imgOptional.get();
            img.setTitle(productImgReq.getTitle());
            img.setImgUrl(productImgReq.getImgUrl());
            img.setFileExtension(productImgReq.getFileExtension());
            ProductImg updatedImg = productImgService.save(img);
            return new GlobalRes<>(HttpStatus.OK, "Successfully updated the product image", updatedImg);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update the product image", null);
    }


    @DeleteMapping("/deleteProductImg/{id}")
    public GlobalRes<String> deleteProductImg(@PathVariable int id) {
        try {
            productImgService.delete(id);
            return new GlobalRes<>(HttpStatus.OK, "Successfully deleted the product image", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to delete the product image", null);
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productId") Integer productId) throws IOException {

        Optional<Product> product = productService.getProductById(productId);
        if (!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Product not found with ID: " + productId);
        }

        String uploadImage = productImgService.saveProductImage(file, product.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }


    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String fileName) throws IOException {
        byte[] imageData = productImgService.getProductImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageData);
    }

    @GetMapping("/sort/{field}/{order}")
    public GlobalRes<List<ProductImg>> getProductImgsWithSort(
            @PathVariable("field") String field,
            @PathVariable("order") String order) {
        List<ProductImg> productImgs = productImgService.findProductImgWithSort(field, order);
        if (productImgs.isEmpty()) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No product images found", null);
        }
        return new GlobalRes<>(HttpStatus.OK, "Product images retrieved successfully", productImgs);
    }

    @GetMapping("/page/{offset}/{size}")
    public GlobalRes<Page<ProductImg>> getProductImgsWithPage(
            @PathVariable("offset") int offset,
            @PathVariable("size") int size) {
        Page<ProductImg> productImgs = productImgService.findProductImgWithPage(offset, size);
        if (productImgs.isEmpty()) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No product images found", null);
        }
        return new GlobalRes<>(HttpStatus.OK, "Product images retrieved successfully", productImgs);
    }

}
