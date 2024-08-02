package com.uth.BE.Controller;

import com.uth.BE.Pojo.Comment;
import com.uth.BE.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    @GetMapping("/readALlComment")
    public List<Comment> readAllComment() {
        return commentService.findAll();
    }

    @GetMapping("/readCommentByID/{id}")
    public Optional<Comment> readCommentById(@PathVariable int id) {
        return commentService.findById(id);
    }

    @DeleteMapping("/deleteComment/{id}")
    public void deleteComment(@PathVariable int id) {
        commentService.delete(id);
    }

    @PostMapping("/updateComment")
    public Comment updateComment(@RequestBody Comment comment) {
        return commentService.update(comment);
    }
}
