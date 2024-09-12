package com.uth.BE.dto.req;

import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class ProductDTO {
    private int productId;
    private Category category;
    private String title;
    private String description;
    private BigDecimal price;
    private String status;
    private User seller;
    private Timestamp create_at;
    private List<String> imageUrls;

    public ProductDTO(int productId, Category category, String title, String description, BigDecimal price, String status, List<String> imageUrls, Timestamp create_at,User seller) {
        this.productId = productId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.imageUrls = imageUrls;
        this.create_at = create_at;
        this.seller = seller;
    }

    public ProductDTO() {
    }
}

//    // Getters
//    public int getProductId() {
//        return productId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public List<String> getImageUrls() {
//        return imageUrls;
//    }
//
//    // Setters
//    public void setProductId(int productId) {
//        this.productId = productId;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public void setImageUrls(List<String> imageUrls) {
//        this.imageUrls = imageUrls;
//    }
//}
