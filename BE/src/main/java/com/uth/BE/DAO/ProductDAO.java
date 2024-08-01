package com.uth.BE.DAO;

import com.uth.BE.Pojo.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ProductDAO {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public ProductDAO(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    // Create: Save a new product
    public void save(Product product) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // Read: Get a list of all products
    public List<Product> getProducts() {
        List<Product> products = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
        return products;
    }

    // Read: Find a product by ID
    public Product findById(int productId) {
        Product product = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            product = em.find(Product.class, productId);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
        return product;
    }

    // Update: Update an existing product
    public void update(Product product) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(product);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    // Delete: Remove a product by ID
    public void delete(int productId) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Product product = em.find(Product.class, productId);
            if (product != null) {
                em.remove(product);
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
