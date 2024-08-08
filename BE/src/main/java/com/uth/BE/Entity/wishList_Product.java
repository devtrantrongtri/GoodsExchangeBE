package com.uth.BE.Entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "wish_list_product")
public class WishListProduct {

    @EmbeddedId
    private WishListProductId id;

    @ManyToOne
    @MapsId("wishListId")
    @JoinColumn(name = "wish_list_id", nullable = false)
    private WishList wishList;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "added_at", updatable = false)
    private Timestamp addedAt;

    // Constructors
    public WishListProduct() {
        super();
    }

    public WishListProduct(WishList wishList, Product product, Timestamp addedAt) {
        this.id = new WishListProductId(wishList.getId(), product.getProductId());
        this.wishList = wishList;
        this.product = product;
        this.addedAt = addedAt;
    }

    // Getters and Setters
    public WishListProductId getId() {
        return id;
    }

    public void setId(WishListProductId id) {
        this.id = id;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
class WishListProductId implements java.io.Serializable {

    @Column(name = "wish_list_id")
    private int wishListId;

    @Column(name = "product_id")
    private int productId;

    // Constructors
    public WishListProductId() {}

    public WishListProductId(int wishListId, int productId) {
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
        WishListProductId that = (WishListProductId) o;
        return wishListId == that.wishListId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wishListId, productId);
    }
}
