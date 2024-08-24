package com.uth.BE.Entity;

import com.uth.BE.Entity.model.FileExtension;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@Table(name = "productimg")
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "productimg_id")
    private int productimg_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "img_url", nullable = false)
    private String img_url;

    @Column(name = "file_extension", nullable = false)
    private FileExtension file_extension;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Timestamp created_at;

    public ProductImg() {
        super();
    }

    public ProductImg(String title, String img_url, FileExtension file_extension) {
        super();
        this.title = title;
        this.img_url = img_url;
        this.file_extension = file_extension;
    }

    public int getProductimgId() {
        return productimg_id;
    }

    public void setProductimgId(int productimg_id) {
        this.productimg_id = productimg_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return img_url;
    }

    public void setImgUrl(String img_url) {
        this.img_url = img_url;
    }

    public FileExtension getFileExtension() {
        return file_extension;
    }

    public void setFileExtension(FileExtension file_extension) {
        this.file_extension = file_extension;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }
}
