/*package com.uth.BE.Service;

import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.WishList;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Repository.WishlistRepository;
import com.uth.BE.Service.Interface.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService implements IWishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WishList save(WishList wishList) {
        return wishlistRepository.save(wishList);
    }

    @Override
    public Optional<WishList> findById(int id) {
        return wishlistRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        wishlistRepository.deleteById(id);
    }

    @Override
    public WishList addProduct(Integer productId, Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (userOpt.isPresent() && productOpt.isPresent()) {
            User user = userOpt.get();
            Product product = productOpt.get();

            // Kiểm tra nếu sản phẩm đã có trong bất kỳ wishlist nào của người dùng này
//            List<WishList> userWishlists = wishlistRepository.findAll();
//            for (WishList wishlist : userWishlists) {
//                if (wishlist.getUser().getUserId() == userId && wishlist.getProduct().getProduct_id() == productId) {
//                    // Nếu sản phẩm đã tồn tại trong wishlist, trả về wishlist đó
//                    return wishlist;
//                }
//            }

            // Nếu sản phẩm chưa có, tạo mới wishlist
            WishList newWishlist = new WishList();
            newWishlist.setUser(user);
            newWishlist.setProduct(product);

            return wishlistRepository.save(newWishlist);
        }

        // Xử lý nếu không tìm thấy user hoặc product
        throw new RuntimeException("User or Product not found");
    }
}
*/

/*
package com.uth.BE.Service;
import java.util.List;
import java.util.Optional;

import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.WishList;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Repository.WishlistRepository;
import com.uth.BE.Service.Interface.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishlistService implements IWishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WishList save(WishList wishList) {
        return wishlistRepository.save(wishList);
    }

    @Override
    public Optional<WishList> findById(int id) {
        return wishlistRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        wishlistRepository.deleteById(id);
    }

    @Override
    public WishList addProduct(Integer productId, Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (userOpt.isPresent() && productOpt.isPresent()) {
            User user = userOpt.get();
            Product product = productOpt.get();

            // Tìm tất cả wishlist của người dùng
            List<WishList> wishLists = wishlistRepository.findAllByUser_UserId(userId);

            // Kiểm tra sự tồn tại của sản phẩm trong các wishlist
            for (WishList wishlist : wishLists) {
                if (wishlist.getProduct().getProduct_id() == productId) {
                    // Nếu sản phẩm đã tồn tại trong wishlist, trả về wishlist đó
                    return wishlist;
                }
            }

            // Nếu không tồn tại wishlist cho sản phẩm, tạo mới
            WishList newWishlist = new WishList();
            newWishlist.setUser(user);
            newWishlist.setProduct(product);
            return wishlistRepository.save(newWishlist);
        }

        throw new RuntimeException("User or Product not found");
    }


}
*/


package com.uth.BE.Service;

import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.WishList;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Repository.WishlistRepository;
import com.uth.BE.Service.Interface.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishlistService implements IWishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WishList save(WishList wishList) {
        return wishlistRepository.save(wishList);
    }

    @Override
    public WishList addProduct(Integer productId, Integer userId) {
        Optional<Product> product = productRepository.findById(productId);
        Optional<User> user = userRepository.findById(userId);

        if (product.isPresent() && user.isPresent()) {
            WishList wishList = wishlistRepository.findById(userId).orElse(new WishList());

            wishList.setUser(user.get());
            wishList.setProduct(product.get());

            return wishlistRepository.save(wishList);
        } else {
            throw new RuntimeException("Product or User not found");
        }
    }

    @Override
    public Optional<WishList> findById(int id) {
        return wishlistRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        wishlistRepository.deleteById(id);
    }

    @Override
    public void createWishList(WishList wishList) {
        try {
            wishlistRepository.save(wishList);
        } catch (Exception e) {
            System.err.println("Error occurred while creating wish list: " + e.getMessage());
        }
    }
}
