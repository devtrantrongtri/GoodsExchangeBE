package com.uth.BE.Service;

import com.uth.BE.Entity.Comment;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.CommentRepository;
import com.uth.BE.Repository.ProductRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductRepository productRepo;

    public CommentService(CommentRepository commentRepo, UserRepository userRepo, ProductRepository productRepo) {
        super();
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepo.save(comment);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepo.findAll();
    }

    @Override
    public Optional<Comment> findById(int id) {
        Optional<Comment> comment = Optional.ofNullable(commentRepo.findById(id).orElse(null));
        return comment;
    }

    @Override
    public void delete(int id) {
        commentRepo.deleteById(id);
    }

    @Override
    public Comment update(Comment comment) {
        Comment exiting = commentRepo.findById(comment.getCommentId()).orElse(null);
        assert exiting != null;
        exiting.setCommentText(comment.getCommentText());
        exiting.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return commentRepo.save(exiting);
    }

    @Override
    public List<Comment> findCommentByUser(int userID) {
        User u = userRepo.findById(userID).orElse(null);
        if (u != null) {
            return commentRepo.findByUser(u);
        } else {
            return List.of();
        }
    }

    @Override
    public List<Comment> findCommentByProduct(int productID) {
        Product p = productRepo.findById(productID).orElse(null);
        if (p != null) {
            return commentRepo.findByProduct(p);
        }
        return List.of();
    }

    @Override
    public List<Comment> findCommentByProductAndUser(int productD, int userID) {
        Product p = productRepo.findById(productD).orElse(null);
        User u = userRepo.findById(userID).orElse(null);
        if (u != null && p != null) {
            return commentRepo.findByUserAndProduct(u, p);
        }
        return List.of();
    }

    @Override
    public List<Comment> searchCommentsByKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }
        return commentRepo.findByTextContains(keyword);
    }

    @Override
    public List<Comment> findCommentWithSort(String field, String order) {
        if (ReflectionUtils.findField(Comment.class, field) == null) {
            throw new IllegalArgumentException("Invalid field name: " + field);
        }

        return commentRepo.findAll(Sort.by(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, field));
    }

    @Override
    public Page<Comment> findCommentWithPage(int offset, int size) {
        return commentRepo.findAll(PageRequest.of(offset,size));
    }

    @Override
    public Page<Comment> findCommentWithPageAndSort(int offset, int size, String field, String order) {
        if (ReflectionUtils.findField(Comment.class, field) == null) {
            throw new IllegalArgumentException("Invalid field name: " + field);
        }

        return commentRepo.findAll(PageRequest.of(offset,size).withSort(Sort.by(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, field)));
    }
}
