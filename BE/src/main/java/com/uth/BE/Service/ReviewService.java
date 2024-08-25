package com.uth.BE.Service;


import com.uth.BE.Entity.Review;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.ReviewRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;


    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository){
        super();
        this.reviewRepository=reviewRepository;
        this.userRepository=userRepository;
    }
    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public void delete(int id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Optional<Review> findById(int id) {
        Optional<Review> review = Optional.ofNullable(reviewRepository.findById(id).orElse(null));
        return review;
    }

    @Override
    public Review update(Review review) {
        Review exiting = reviewRepository.findById(review.getReviewId()).orElse(null);
        exiting.setRating(review.getRating());
        exiting.setReviewText(review.getReviewText());
        exiting.setCreatedAt(new Timestamp(review.getCreatedAt().getTime()));
        return reviewRepository.save(exiting);
    }


    @Override
    public List<Review> getReportByUser(int userID) {
        User u = userRepository.findById(userID).orElse(null);
        if (u != null) {
            return reviewRepository.findByUser(u);
        } else {
            return List.of();
        }
    }

    @Override
    public List<Review> getReviewByUserName(String username) {
        User u = userRepository.findByUsername(username).orElse(null);
        if (u != null) {
            return reviewRepository.findByUser(u);
        } else {
            return List.of();
        }
    }


}
