package com.uth.BE.Service;

import com.uth.BE.Entity.Comment;
import com.uth.BE.Repository.CommentRepository;
import com.uth.BE.Service.Interface.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository repo;

    public CommentService(CommentRepository repo) {
        super();
        this.repo = repo;
    }

    @Override
    public Comment save(Comment comment) {
        return repo.save(comment);
    }

    @Override
    public List<Comment> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Comment> findById(int id) {
        Optional<Comment> comment = Optional.ofNullable(repo.findById(id).orElse(null));
        return  comment;
    }

    @Override
    public void delete(int id){
        repo.deleteById(id);
    }

    @Override
    public Comment update(Comment comment) {
        Comment exiting = repo.findById(comment.getCommentId()).orElse(null);
        exiting.setCommentText(comment.getCommentText());
        exiting.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return repo.save(exiting);
    }
}
