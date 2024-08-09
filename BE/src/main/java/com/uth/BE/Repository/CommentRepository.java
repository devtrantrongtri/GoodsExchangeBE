package com.uth.BE.Repository;

import com.uth.BE.Entity.Comment;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByUser(User user);
    List<Comment> findByProduct(Product product);
}
