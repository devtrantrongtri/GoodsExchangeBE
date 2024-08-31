//package com.uth.BE.Repository;
//
//import com.uth.BE.Entity.WishList;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface WishlistRepository extends JpaRepository<WishList, Integer> {
//
//    // Sửa lại tên phương thức để phù hợp với tên thuộc tính `product_id` trong `Product`
//
//}
// WishlistRepository.java

/*package com.uth.BE.Repository;

import com.uth.BE.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findAllByUser_UserId(int userId);
}
*/
package com.uth.BE.Repository;

import com.uth.BE.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<WishList, Integer> {
    }