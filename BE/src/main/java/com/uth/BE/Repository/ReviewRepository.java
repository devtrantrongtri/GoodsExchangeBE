package com.uth.BE.Repository;


import com.uth.BE.Entity.Review;
import com.uth.BE.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    List<Review> findByUser(User u);
}
