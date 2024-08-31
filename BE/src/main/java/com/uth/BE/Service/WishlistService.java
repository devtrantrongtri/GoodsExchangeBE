
package com.uth.BE.Service;

import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.WishList;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Repository.WishlistRepository;
import com.uth.BE.Service.Interface.IWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
            List<WishList> userWishLists = wishlistRepository.findAll(); // Lấy tất cả các wishlist

            // Kiểm tra xem sản phẩm đã tồn tại trong bất kỳ wishlist nào của người dùng hay chưa
            for (WishList wishList : userWishLists) {
                if (wishList.getUser().getUserId() == userId &&
                        wishList.getProduct().getProduct_id() == productId) {
                    throw new RuntimeException("Product already exists in wishlist");
                }
            }

            // Nếu sản phẩm chưa tồn tại, tạo mới một wishlist
            WishList newWishList = new WishList();
            newWishList.setUser(user.get());
            newWishList.setProduct(product.get());

            return wishlistRepository.save(newWishList);
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
        // This method can be used if you need to save a wishList directly
        try {
            wishlistRepository.save(wishList);
        } catch (Exception e) {
            System.err.println("Error occurred while creating wish list: " + e.getMessage());
        }
    }

    public void deleteWishListById(int id) {
        try {
            wishlistRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Error occurred while deleting wish list: " + e.getMessage());
        }
    }

    public List<WishList> getAllWishlists() {
        return wishlistRepository.findAll();
    }

public void updateWishList(Integer wishListId, Integer newProductId) {
    if (newProductId == null) {
        throw new IllegalArgumentException("Product ID cannot be null");
    }

    WishList wishList = wishlistRepository.findById(wishListId)
            .orElseThrow(() -> new RuntimeException("WishList not found"));
    Product newProduct = productRepository.findById(newProductId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    wishList.setProduct(newProduct);
    wishlistRepository.save(wishList);
}

    public void addProductToWishList(int userId, int productId) {
        // Tìm User và Product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Lấy tất cả WishList của người dùng
        List<WishList> userWishLists = wishlistRepository.findAll();
        boolean productExistsInWishList = false;

        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách yêu thích của người dùng chưa
        for (WishList wishList : userWishLists) {
            if (wishList.getUser().getUserId() == userId && wishList.getProduct().getProduct_id() == productId) {
                productExistsInWishList = true;
                break;
            }
        }

        if (productExistsInWishList) {
            throw new RuntimeException("Product already in the wishlist");
        } else {
            // Tạo mới và lưu WishList
            WishList newWishList = new WishList();
            newWishList.setUser(user);
            newWishList.setProduct(product);
            wishlistRepository.save(newWishList);
        }
    }


    public List<Product> getProductsInWishlist(int wishlistId) {
        WishList wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new RuntimeException("Wishlist not found"));
        return List.copyOf(wishlist.getProducts());
    }

    // Remove product from wishlist
    public void removeProductFromWishlist(int wishlistId, int productId) {
        WishList wishlist = wishlistRepository.findById(wishlistId).orElseThrow(() -> new RuntimeException("Wishlist not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        wishlist.getProducts().remove(product);
        wishlistRepository.save(wishlist);
    }

    public String getWishlistAnalytics(int wishlistId) {
        // Get all wishlists
        List<WishList> allWishlists = getAllWishlists();

        // Filter the wishlist based on wishlistId
        WishList wishlist = allWishlists.stream()
                .filter(wl -> wl.getId() == wishlistId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        // Get products from the wishlist
        Product product = wishlist.getProduct();

        if (product == null) {
            return "No products found in the wishlist.";
        }

        // Calculate product count and total value
        int productCount = 1; // Since there's only one product per wishlist in your structure
        double totalValue = product.getPrice().doubleValue();

        return String.format("Total Products: %d, Total Value: %.2f", productCount, totalValue);
    }

    // Sort wishlist products by price ascending
    public List<Product> sortWishlistProducts(int wishlistId) {
        WishList wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        Set<Product> products = wishlist.getProducts();
        List<Product> productList = new ArrayList<>(products);

        // In ra số lượng sản phẩm
        System.out.println("Number of products in wishlist: " + productList.size());

        // Nếu danh sách sản phẩm trống, trả về danh sách trống
        if (productList.isEmpty()) {
            System.out.println("No products found in the wishlist.");
            return Collections.emptyList();
        }

        // Sắp xếp danh sách sản phẩm theo giá
        List<Product> sortedProducts = productList.stream()
                .sorted(Comparator.comparingDouble(product -> product.getPrice().doubleValue()))
                .collect(Collectors.toList());

        // Nếu danh sách đã sắp xếp là rỗng, trả về danh sách ban đầu
        if (sortedProducts.isEmpty()) {
            System.out.println("Sorted products are empty, returning initial product list.");
            return productList;
        }

        System.out.println("Products before sorting: " + productList);
        System.out.println("Products after sorting: " + sortedProducts);

        return sortedProducts;
    }

}
