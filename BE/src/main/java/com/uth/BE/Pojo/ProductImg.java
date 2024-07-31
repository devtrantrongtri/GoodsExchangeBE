package com.uth.BE.Pojo;

import com.uth.BE.Pojo.model.FileExtension;
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
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "img_url", nullable = false)
    private String img_url;

    @Column(name = "file_extension", nullable = false)
    private FileExtension file_extension;

    @GeneratedValue(strategy = GenerationType.AUTO)
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



}
