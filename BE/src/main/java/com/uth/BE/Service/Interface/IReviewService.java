package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {
    public List<Review> findAll();
    public Review save(Review review);
    public void delete(int id);
    public Optional <Review> findById(int id);
    public Review update(Review review);

    List<Review> getReportByUser(int userID);

    List<Review> getReviewByUserName(String username);
}
