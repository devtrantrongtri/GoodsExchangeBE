package com.uth.BE.dto.req;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {
    private int productId;
    private String title;
    private String description;
    private BigDecimal price;
    private String status;
    private List<String> imageUrls;

    public ProductDTO(int productId, String title, String description, BigDecimal price, String status, List<String> imageUrls) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.imageUrls = imageUrls;
    }

    // Getters
    public int getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
