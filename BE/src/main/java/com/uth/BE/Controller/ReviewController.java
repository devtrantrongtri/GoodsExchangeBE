package com.uth.BE.Controller;

import com.uth.BE.Entity.*;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.Interface.IReviewService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.ReviewService;
import com.uth.BE.dto.req.ReviewReq;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final IProductService productService;
    private final IUserService userService;
    private final IReviewService reviewService;

    @Autowired
    public ReviewController(IProductService productService, IUserService userService, IReviewService reviewService) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.userService = userService;
    }


    @PostMapping()
    public GlobalRes<Review> createReview(@RequestBody ReviewReq reviewReq) {
        // Tìm kiếm Product và User theo ID
        Optional<Product> productOpt = productService.getProductById(reviewReq.getProductID());
        Optional<User> userOpt = userService.getUserById(reviewReq.getUserID());

        // Kiểm tra nếu Product không tồn tại
        if (!productOpt.isPresent()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", null);
        }

        // Kiểm tra nếu User không tồn tại
        if (!userOpt.isPresent()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", null);
        }

        // Tạo Review mới với Product và User đã tìm thấy
        Review rv = new Review(reviewReq.getReviewText(), reviewReq.getRating());
        rv.setProduct(productOpt.get());
        rv.setUser(userOpt.get());

        // Lưu Review vào cơ sở dữ liệu
        Review savedReview = reviewService.save(rv);

        // Kiểm tra nếu Review đã được lưu thành công
        if (savedReview != null) {
            return new GlobalRes<>(HttpStatus.CREATED, "Successfully created the review", savedReview);
        }

        // Trả về phản hồi nếu việc lưu Review không thành công
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to create the review", null);
    }


    @GetMapping("/{id}")
    public GlobalRes<Optional<Review>> findReviewById(@PathVariable int id) {
        Optional<Review> reviewOptional = reviewService.findById(id);
        if (reviewOptional.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get this review", reviewOptional);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get this review,null", null);

    }

    @GetMapping("/user/{id}")
    public GlobalRes<List<Review>> getReviewByUser(@PathVariable("id") int userID) {
        List<Review> rv = reviewService.getReportByUser(userID);
        if (rv.isEmpty() || rv == null) {
            return new GlobalRes<>(HttpStatus.OK, "List empty", rv);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "All reviews read successfully", rv);
        }
    }

    @GetMapping("/username/{username}")
    public GlobalRes<List<Review>> searchReviewByUsername(@PathVariable("username") String username) {
        List<Review> rv = reviewService.getReviewByUserName(username);
        if (rv.isEmpty() || rv == null) {
            return new GlobalRes<>(HttpStatus.OK, "List empty", rv);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "All review read successfully", rv);
        }
    }

    @GetMapping()
    public GlobalRes<List<Review>> findAllReview() {
        List<Review> reviews = reviewService.findAll();
        if (reviews != null && !reviews.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get all reviews", reviews);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get all reviews", null);
    }

//    @PutMapping("/updateReview")
//    public GlobalRes<Review> updatedReview(@RequestBody Review review) {
//        Review rv = reviewService.update(review);
//        if (rv != null) {
//            return new GlobalRes<>(HttpStatus.OK, "Successfully update this review", null);
//        } else {
//            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update this review", null);
//        }
//    }

    @DeleteMapping("/deleteReviewById/{id}")
    public GlobalRes<Review> deleteReview(@PathVariable int id) {
        try {
            reviewService.delete(id);
            return new GlobalRes<>(HttpStatus.OK, "Successfully delete this review", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to delete this review", null);
        }
    }

    @PutMapping("/updateReview/{id}")
    public GlobalRes<Review> updateReport(@PathVariable int id, @RequestBody Review review) {
        Optional<Review> reviewOptional = reviewService.findById(id);
        if (reviewOptional.isPresent()) {
            Review rv = reviewOptional.get();
            rv.setRating(review.getRating());
            rv.setReviewText(review.getReviewText());
            Review update = reviewService.save(rv);

            return new GlobalRes<>(HttpStatus.OK, "Successfully updated the review", update);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update the review", null);
    }
}