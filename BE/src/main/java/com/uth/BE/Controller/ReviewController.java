package com.uth.BE.Controller;

import com.uth.BE.Pojo.Review;
import com.uth.BE.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/createReview")
    public Review createReview(@RequestBody Review review){ reviewService.save(review);
        return review;
    }

    @GetMapping("/findReviewById/{reviewId}")
    public Optional<Review> findReviewById(@PathVariable int reviewId){return reviewService.findById(reviewId);}

    @GetMapping("/findAllReview")
    public List<Review> findAllReview(){return reviewService.findAll();}

    @PostMapping("/updateReview")
    public Review updateReview(@RequestBody Review review){reviewService.update(review);
        return review;
    }

    @PostMapping("/deleteReviewById/{reviewId}")
    public void deleteReview(@PathVariable int reviewId) {reviewService.delete(reviewId);}
}
