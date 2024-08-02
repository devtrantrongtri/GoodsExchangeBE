//package com.uth.BE.DAO;
//
//import com.uth.BE.Pojo.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface UserDAO extends JpaRepository<User, Integer> {
//}

package com.uth.BE.DAO;

import com.uth.BE.Pojo.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class UserDAO {
    private static EntityManager em;
    private static EntityManagerFactory emf;

    public UserDAO(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    // Create: Save a new user
    public void save(User user) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // Read: Get a list of all users
    public List<User> getUsers() {
        List<User> users = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
        return users;
    }

    // Read: Find a user by ID
    public User findById(int userId) {
        User user = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            user = em.find(User.class, userId);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
        return user;
    }

    // Update: Update an existing user
    public void update(User user) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            User u = em.find(User.class, user.getUserId());
            if (u != null) {
                u.setUsername(user.getUsername());
                u.setPassword(user.getPassword());
                u.setEmail(user.getEmail());
                // Update other fields as necessary
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    // Delete: Remove a user by ID
    public void delete(int userId) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            User u = em.find(User.class, userId);
            if (u != null) {
                em.remove(u);
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
    }
}
