package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Comment;

import java.util.List;
import java.util.Optional;

public interface ICommentService {
    public Comment save(Comment comment);

    public List<Comment> findAll();

    public Optional<Comment> findById(int id);

    public void delete(int id);

    public Comment update(Comment comment);

    public List<Comment> findCommentByUser(int userID);

    public List<Comment> findCommentByProduct(int productD);

    public List<Comment> findCommentByProductAndUser(int productD, int userID);

    public List<Comment> searchCommentsByKeyword(String keyword);
}
