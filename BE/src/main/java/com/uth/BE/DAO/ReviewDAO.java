package com.uth.BE.DAO;

import com.uth.BE.Pojo.Report;
import com.uth.BE.Pojo.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class ReviewDAO {
    private static EntityManager em;
    private static EntityManagerFactory emf;

    public ReviewDAO(String persistenceName){
        emf= Persistence.createEntityManagerFactory(persistenceName);
    }

    //Save
    public void save(Review review){
        try {
            em= emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(review);
            em.getTransaction().commit();
        }catch  (Exception ex){
            em.getTransaction().rollback();
            System.out.println("Error " + ex.getMessage());
        }finally {
            em.close();
        }
    }

    //Get
    public List<Review> getReview(){
        List<Review> reviews=null;

        try{
            em=emf.createEntityManager();
            em.getTransaction().begin();
            reviews=em.createQuery("SELECT rv FROM Review rv",Review.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            em.close();
        }
        return reviews;
    }

    //Delete
    public void delete(int reviewId) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Review rv = em.find(Review.class, reviewId);
            if (rv != null) { // Check if reviews exists before deleting
                em.remove(rv);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    //Find
    public Review findById(int reviewId) {
        Review review = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Review rv = em.find(Review.class, reviewId);
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            em.close();
        }

        return review;
    }

    //Update
    public void update(Review review){
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Review rv = em.find(Review.class,review.getReview_id());
            if (rv != null) {
                rv.setRating(review.getRating());
                rv.setReview_text(review.getReview_text());
                rv.setStatus(rv.getStatus());
                em.getTransaction().commit();
            }
        }catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error " + ex.getMessage());
        } finally {
            em.close();
        }
    }
}
