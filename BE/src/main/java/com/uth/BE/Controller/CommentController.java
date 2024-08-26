package com.uth.BE.Controller;

import com.uth.BE.Entity.Comment;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.ICommentService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.ProductService;
import com.uth.BE.dto.req.CommentReq;
import com.uth.BE.dto.req.PaginationRequest;
import com.uth.BE.dto.res.GlobalRes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        Optional<User> user = userService.getUserById(commentReq.getUserId());
        Optional<Product> product = productService.getProductById(commentReq.getProductId());

        if (user.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", null);
        }
        if (product.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", null);
        }

        Comment c = new Comment(commentReq.getMessage());
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
        if (commentOptional.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get this comment", commentOptional);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get this comment", null);
    }

    @GetMapping("/user/{id}")
    public GlobalRes<List<Comment>> getCommentByUser(@PathVariable("id") int userID) {
        List<Comment> c = commentService.findCommentByUser(userID);
        if (c.isEmpty() || c == null) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "List empty", c);
        }
        return new GlobalRes<List<Comment>>(HttpStatus.OK, "All comments read successfully", c);
    }

    @GetMapping("user/{userId}/post/{postId}")
    public GlobalRes<List<Comment>> getCommentsByUserAndProduct(@PathVariable("userId") int userID, @PathVariable("postId") int productID) {
        List<Comment> comments = commentService.findCommentByProductAndUser(productID, userID);
        if (comments.isEmpty()) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "List empty", null);
        }
        return new GlobalRes<>(HttpStatus.OK, "Successfully fetched comments for the user and product", comments);
    }

    @GetMapping("/post/{id}/count")
    public GlobalRes<Long> countCommentsByPost(@PathVariable("id") int postId) {
        long count = commentService.findCommentByProduct(postId).size();
        return new GlobalRes<>(HttpStatus.OK, "Successfully fetched comment count for the product", count);
    }

    @GetMapping("/search")
    public GlobalRes<List<Comment>> searchComments(@RequestParam String keyword) {
        List<Comment> comments = commentService.searchCommentsByKeyword(keyword);

        if (!comments.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully fetched comments containing the keyword", comments);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No comments found containing the keyword", null);
    }

    @GetMapping("/post/{id}")
    public GlobalRes<List<Comment>> getCommentByPost(@PathVariable("id") int productID) {
        List<Comment> c = commentService.findCommentByProduct(productID);
        if (c.isEmpty() || c == null) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "List empty", c.stream().toList());
        }
        return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", c.stream().toList());
    }

    @DeleteMapping("/deleteComment/{id}")
    public GlobalRes<Comment> deleteComment(@PathVariable("id") int id) {
        if (commentService.findById(id).isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Comment not found", null);
        }
        try {
            commentService.delete(id);
            return new GlobalRes<>(HttpStatus.OK, "Successfully delete this comment", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to delete this comment", null);
        }
    }

    @PutMapping("/updateComment/{id}")
    public GlobalRes<Comment> updateComment(@PathVariable("id") int commentId, @RequestBody CommentReq commentReq) {
        Optional<Comment> existingComment = commentService.findById(commentId);

        if (existingComment.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Comment not found", null);
        }

        Comment updateComment = existingComment.get();
        updateComment.setCommentText(commentReq.getMessage());

        Optional<User> user = userService.getUserById(commentReq.getUserId());
        Optional<Product> product = productService.getProductById(commentReq.getProductId());

        if (user.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", null);
        }

        if (product.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", null);
        }

        updateComment.setUser(user.get());
        updateComment.setProduct(product.get());

        Comment updatedComment = commentService.update(updateComment);

        if (updatedComment != null) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully updated this comment", updatedComment);
        }

        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update this comment", null);
    }

    @GetMapping("/sort/{field}/{order}")
    private GlobalRes<List<Comment>> getCommentsWithSort(@PathVariable("field") String field, @PathVariable("order") String order) {
        List<Comment> comments = commentService.findCommentWithSort(field, order);
        if (comments.isEmpty()) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No comments found", null);
        }
        return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", comments);
    }

    @GetMapping("/page/{offset}/{size}")
    private GlobalRes<Page<Comment>> getCommentsWithPage(@PathVariable("offset") int offset, @PathVariable("size") int size) {
        Page<Comment> comments = commentService.findCommentWithPage(offset,size);
        if (comments.isEmpty()) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No comments found", null);
        }
        return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", comments);
    }

    @GetMapping("/sortAndPage")
    private GlobalRes<Page<Comment>> getCommentsWithSortAndPage(@Valid @RequestBody PaginationRequest req) {
        try {
            Page<Comment> commentsPage = commentService.findCommentWithPageAndSort(
                    req.getOffset(),
                    req.getPageSize(),
                    req.getField(),
                    req.getOrder()
            );
            if (commentsPage.getTotalElements() > 0) {
                return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", commentsPage);
            }
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No comments found", null);
        } catch (IllegalArgumentException e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}
