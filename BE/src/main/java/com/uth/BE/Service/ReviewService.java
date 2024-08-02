package com.uth.BE.Service;


import com.uth.BE.Pojo.Review;
import com.uth.BE.Repository.ReviewRepository;
import com.uth.BE.Service.Interface.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewRepository repo;

    public ReviewService(ReviewRepository repo){
        super();
        this.repo=repo;
    }

    @Override
    public List<Review> findAll() {
        return repo.findAll();
    }

    @Override
    public void save(Review review) {
        repo.save(review);
    }

    @Override
    public void delete(int reviewId) {
        repo.deleteById(reviewId);
    }

    @Override
    public Optional<Review> findById(int reviewId) {
        Optional<Review> review = Optional.ofNullable(repo.findById(reviewId).orElse(null));
        return  review;    }

    @Override
    public void update(Review review) {
        Review exiting = repo.findById(review.getReview_id()).orElse(null);
        exiting.setReview_text(review.getReview_text());
        exiting.setRating(review.getRating());
        exiting.setCreated_at(new Timestamp(review.getCreated_at().getTime()));
        exiting.setStatus(review.getStatus());
        repo.save(exiting);
    }
}
