package com.uth.BE.Entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int comment_id;

    //    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_id")
    private Product product;

    //    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment_text", columnDefinition = "TEXT", nullable = false)
    private String text;

    //    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdAt;

    //    public Comment() {
//        super();
//    }
//
    public Comment(String comment_text) {
        super();
        this.text = comment_text;
    }

    public int getCommentId() {
        return comment_id;
    }

    public void setCommentId(int comment_id) {
        this.comment_id = comment_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentText() {
        return text;
    }

    public void setCommentText(String comment_text) {
        this.text = comment_text;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.createdAt = created_at;
    }
}
