package com.uth.BE.Pojo;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "wish_list")
public class WishList {

    @EmbeddedId
    private WishListId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Constructors
    public WishList() {}

    public WishList(User user, Product product) {
        this.id = new WishListId(user.getUserId(), product.getProductId());
        this.user = user;
        this.product = product;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public WishListId getId() {
        return id;
    }

    public void setId(WishListId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Override equals and hashCode for composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishList wishList = (WishList) o;
        return Objects.equals(id, wishList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

@Embeddable
class WishListId implements java.io.Serializable {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "product_id")
    private int productId;

    // Constructors, getters, setters, equals, and hashCode
    public WishListId() {}

    public WishListId(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishListId that = (WishListId) o;
        return userId == that.userId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
}
