package com.uth.BE.Service;

import com.uth.BE.Entity.WishList;
import com.uth.BE.Entity.Product;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.WishlistRepository;
import com.uth.BE.Service.Interface.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService implements IWishlistService {

    @Autowired
    private WishlistRepository repoWl;

    @Autowired
    private ProductRepository repoPr;

    public WishlistService(WishlistRepository repoWl, ProductRepository repoPr) {
        this.repoWl = repoWl;
        this.repoPr = repoPr;
    }

    @Override
    public WishList save(WishList wishList) {
        return repoWl.save(wishList);
    }

    @Override
    @Transactional
    public WishList addProduct(Product product, WishList wishList) {
        Optional<WishList> optionalWishlist = repoWl.findById(wishList.getId());
        Optional<Product> optionalProduct = repoPr.findById(product.getProduct_id());

        if (optionalWishlist.isPresent() && optionalProduct.isPresent()) {
            WishList existingWishList = optionalWishlist.get();
            Product existingProduct = optionalProduct.get();
            existingWishList.getProducts().add(existingProduct);
            return repoWl.save(existingWishList);
        }
        return null;
    }

    @Override
    public Optional<WishList> findById(int id) {
        return repoWl.findById(id);
    }

    @Override
    public void delete(int id) {
        repoWl.deleteById(id);
    }

    // Method not in IWishlistService, so no @Override annotation
    @Transactional
    public void removeProductFromWishlist(int wishlistId, int productId) {
        Optional<WishList> optionalWishlist = repoWl.findById(wishlistId);
        if (optionalWishlist.isPresent()) {
            WishList existingWishList = optionalWishlist.get();
            existingWishList.getProducts().removeIf(product -> product.getProduct_id() == productId);
            repoWl.save(existingWishList);
        }
    }

    // Method not in IWishlistService, so no @Override annotation
    public List<WishList> findByUserId(int userId) {
        return repoWl.findByUserUserId(userId);
    }
}
