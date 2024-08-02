package com.uth.BE.DAO;

import com.uth.BE.Pojo.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileDAO extends JpaRepository<UserProfile, Integer> {
}

//package com.uth.BE.DAO;
//
//import com.uth.BE.Pojo.UserProfile;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//import java.util.List;
//
//public class UserProfileDAO {
//    private EntityManagerFactory emf;
//
//    public UserProfileDAO(String persistenceUnitName) {
//        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
//    }
//
//    // Create: Save a new UserProfile
//    public void save(UserProfile userProfile) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.persist(userProfile);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//            System.out.println("Error: " + e.getMessage());
//        } finally {
//            em.close();
//        }
//    }
//
//    // Read: Get a list of all UserProfiles
//    public List<UserProfile> getUserProfiles() {
//        EntityManager em = emf.createEntityManager();
//        try {
//            return em.createQuery("FROM UserProfile", UserProfile.class).getResultList();
//        } finally {
//            em.close();
//        }
//    }
//
//    // Read: Find a UserProfile by profile ID
//    public UserProfile findById(int profileId) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            return em.find(UserProfile.class, profileId);
//        } finally {
//            em.close();
//        }
//    }
//
//    // Update: Update an existing UserProfile
//    public void update(UserProfile userProfile) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            UserProfile existingProfile = em.find(UserProfile.class, userProfile.getProfileId());
//            if (existingProfile != null) {
//                existingProfile.setBio(userProfile.getBio());
//                existingProfile.setProfileImageUrl(userProfile.getProfileImageUrl());
//                existingProfile.setFile_extension(userProfile.getFile_extension());
//                em.merge(existingProfile);
//                em.getTransaction().commit();
//            }
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//            System.out.println("Error: " + e.getMessage());
//        } finally {
//            em.close();
//        }
//    }
//
//    // Delete: Remove a UserProfile by profile ID
//    public void delete(int profileId) {
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            UserProfile profile = em.find(UserProfile.class, profileId);
//            if (profile != null) {
//                em.remove(profile);
//                em.getTransaction().commit();
//            }
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//            System.out.println("Error: " + e.getMessage());
//        } finally {
//            em.close();
//        }
//    }
//}
