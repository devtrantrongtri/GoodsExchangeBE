package com.uth.BE.Service.Interface;

import com.uth.BE.Pojo.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    public List<Review> findAll();
    public void save(Review review);
    public void delete(int reviewId);
    public Optional <Review> findById(int reviewId);
    public void update(Review review);
}
