package com.uth.BE.DAO;

import com.uth.BE.Pojo.Product;
import com.uth.BE.Pojo.Report;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import  jakarta.persistence.Persistence;

import java.util.List;

public class ReportDAO {
    private static EntityManager em;
    private static EntityManagerFactory emf;

    public ReportDAO(String persistenceName){
        emf=Persistence.createEntityManagerFactory(persistenceName);
    }

    //Save
    public void save(Report report){
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(report);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error " + ex.getMessage());
        }finally {
            em.close();
        }
    }

    //Get
    public List<Report> getReport(){
        List<Report> reports =null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            reports = em.createQuery("SELECT rp FROM Report rp",Report.class).getResultList();
            em.getTransaction().commit();
        }catch (Exception ex){
            System.out.println("Error " + ex.getMessage());
        }finally {
            em.close();
        }
        return reports;

    }

    //Delete
    public void delete(int reportId){
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Report rp = em.find(Report.class, reportId);
            if (rp != null) {// Check if report exists before deleting
                em.remove(rp);
            }
            em.getTransaction().commit();
        } catch (Exception ex){
            System.out.println("Error " + ex.getMessage());
        }finally {
            em.close();
        }
    }

    //Update
    public void update(Report report) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Report rp = em.find(Report.class, report.getReport_id());
            if (rp != null) {
                rp.setReport_img(report.getReport_img());
                rp.setReport_title(report.getReport_title());
                rp.setReport_reason(report.getReport_title());
                em.getTransaction().commit();
            }
            } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    //Find
    public Report findById(int reportId){
        Report report =null;
        try{
            em = emf.createEntityManager();
            em.getTransaction().begin();
            report=em.find(Report.class,reportId);
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            em.close();
        }
        return report;
    }

}
