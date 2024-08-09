package com.uth.BE.Controller;

import com.uth.BE.Entity.Comment;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.ICommentService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.ProductService;
import com.uth.BE.dto.req.CommentReq;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private ICommentService commentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ProductService productService;

    @PostMapping()
    public GlobalRes<Comment> createComment(@RequestBody CommentReq commentReq) {
        Comment c = new Comment(commentReq.getMessage());
        Optional<User> user = userService.getUserById(commentReq.getUserId());
        Optional<Product> product = productService.getProductById(commentReq.getProductId());
        c.setUser(user.orElse(null));
        c.setProduct(product.orElse(null));

        Comment savedComment = commentService.save(c);

        if (savedComment != null) {
            return new GlobalRes<>(HttpStatus.CREATED, "Successfully create this comment", null);
        }

        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed create this comment", null);
    }

    @GetMapping()
    public GlobalRes<List<Comment>> readAllComment() {
        List<Comment> c = commentService.findAll();
        if (!c.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get all comment", c);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get all comment", null);
    }

    @GetMapping("/{id}")
    public GlobalRes<Optional<Comment>> readCommentById(@PathVariable int id) {
        Optional<Comment> commentOptional = commentService.findById(id);
        if(commentOptional.isPresent()){
            return new GlobalRes<>(HttpStatus.OK, "Successfully get this comment", commentOptional);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get this comment", null);
    }

    @GetMapping("/user/{id}")
    public GlobalRes<List<Comment>> getCommentByUser(@PathVariable("id") int userID) {
        List<Comment> c = commentService.findCommentByUser(userID);
        if(c.isEmpty() || c == null){
            return new GlobalRes<>(HttpStatus.BAD_REQUEST,"List empty",c);
        }
        return new GlobalRes<List<Comment>>(HttpStatus.OK, "All comments read successfully", c);
    }

    @GetMapping("/post/{id}")
    public GlobalRes<List<Comment>> getCommentByPost(@PathVariable("id") int productID) {
        List<Comment> c = commentService.findCommentByProduct(productID);
        if(c.isEmpty() || c == null){
            return new GlobalRes<>(HttpStatus.BAD_REQUEST,"List empty",c);
        }
        return new GlobalRes<List<Comment>>(HttpStatus.OK, "All comments read successfully", c);
    }

    @DeleteMapping("/deleteComment/{id}")
    public GlobalRes<Comment> deleteComment(@PathVariable int id) {
        try{
            commentService.delete(id);
            return new GlobalRes<>(HttpStatus.OK, "Successfully delete this comment", null);
        }catch (Exception e){
            e.printStackTrace();
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to delete this comment", null);
        }
    }

    @PostMapping("/updateComment")
    public GlobalRes<Comment> updateComment(@RequestBody Comment comment) {
        Comment c = commentService.update(comment);
        if(c != null){
            return new GlobalRes<>(HttpStatus.OK, "Successfully update this comment", null);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update this comment", null);
    }
}
