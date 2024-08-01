package com.uth.BE.DAO;

import com.uth.BE.Pojo.ProductImg;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ProductImgDAO {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public ProductImgDAO(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    // Create: Save a new product image
    public void save(ProductImg productImg) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(productImg);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // Read: Get a list of all product images
    public List<ProductImg> getProductImgs() {
        List<ProductImg> productImgs = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            productImgs = em.createQuery("SELECT p FROM ProductImg p", ProductImg.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
        return productImgs;
    }

    // Read: Find a product image by ID
    public ProductImg findById(int productImgId) {
        ProductImg productImg = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            productImg = em.find(ProductImg.class, productImgId);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
        return productImg;
    }

    // Update: Update an existing product image
    public void update(ProductImg productImg) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(productImg);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    // Delete: Remove a product image by ID
    public void delete(int productImgId) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            ProductImg productImg = em.find(ProductImg.class, productImgId);
            if (productImg != null) {
                em.remove(productImg);
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
