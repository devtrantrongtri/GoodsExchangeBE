package com.uth.BE.Service;


import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.Review;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.ReviewRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
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
//        exiting.setCreatedAt(new Timestamp(review.getCreatedAt().getTime()));
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

    @Override
    public List<Review> getALLReviewWithSort(String field, String order) {
        // asc -> tăng, desc -> down
        Sort.Direction sortDirection = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        // check filed is correctly
        Field reviewField = ReflectionUtils.findField(Review.class, field);

        if (reviewField == null) {
            throw new IllegalArgumentException("Field '" + field + "' does not exist in Review class");
//             return reviewRepository.findAll();
        }
        // Tạo đối tượng Sort với filed  và order
        Sort sort = Sort.by(sortDirection, field);

        return reviewRepository.findAll(sort);
    }


    @Override
    public Page<Review> getAllReviewsWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties) {
        Sort.Direction directed = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, directed, properties);
        return reviewRepository.findAll(pageable);
    }


}
