package com.uth.BE.Entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "wish_list_product")
public class wishListProduct {

    @EmbeddedId
    private wishListProductId id;

    @ManyToOne
    @MapsId("wishListId")
    @JoinColumn(name = "wish_list_id", nullable = false)
    private wishList wishList;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private product product;

    @Column(name = "added_at", updatable = false)
    private Timestamp addedAt;

    // Constructors
    public wishListProduct() {
        super();
    }

    public wishListProduct(wishList wishList, product product, Timestamp addedAt) {
        this.id = new wishListProductId(wishList.getId(), product.getProductId());
        this.wishList = wishList;
        this.product = product;
        this.addedAt = addedAt;
    }

    // Getters and Setters
    public wishListProductId getId() {
        return id;
    }

    public void setId(wishListProductId id) {
        this.id = id;
    }

    public wishList getWishList() {
        return wishList;
    }

    public void setWishList(wishList wishList) {
        this.wishList = wishList;
    }

    public product getProduct() {
        return product;
    }

    public void setProduct(product product) {
        this.product = product;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }
}

@Embeddable
class wishListProductId implements java.io.Serializable {

    @Column(name = "wish_list_id")
    private int wishListId;

    @Column(name = "product_id")
    private int productId;

    // Constructors
    public wishListProductId() {}

    public wishListProductId(int wishListId, int productId) {
        this.wishListId = wishListId;
        this.productId = productId;
    }

    // Getters and Setters
    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    // Override equals and hashCode for composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        wishListProductId that = (wishListProductId) o;
        return wishListId == that.wishListId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wishListId, productId);
    }
}
