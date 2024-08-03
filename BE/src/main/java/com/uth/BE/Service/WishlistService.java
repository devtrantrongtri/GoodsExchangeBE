package com.uth.BE.Service;

import com.uth.BE.Pojo.Product;
import com.uth.BE.Pojo.WishList;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.WishlistRepository;
import com.uth.BE.Service.Interface.IWishlistService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

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

//    @Transactional
//    @Override
//    public WishList addProduct(Product product, WishList wishList) {
//        Optional<WishList> optionalWishlist = repoWl.findById(wishList.getId());
//        Optional<Product> optionalProduct = repoPr.findById(product.getProduct_id());
//
//        if (optionalWishlist.isPresent() && optionalProduct.isPresent()) {
//            WishList existingWishList = optionalWishlist.get();
//            Product existingProduct = optionalProduct.get();
//            existingWishList.getProduct().add(existingProduct);
//
//            return repoWl.save(existingWishList);
//        }
//        return null;
//    }

//    public Set<Product> findAllProduct(WishList wishList) {
//        Optional<WishList> opWishlist = repoWl.findById(wishList.getId());
//        return opWishlist.map(WishList::getProduct).orElse(null);
//    }

    public Optional<WishList> findById(int id) {
        return repoWl.findById(id);
    }

    public void delete(int id) {
        repoWl.deleteById(id);
    }
}
