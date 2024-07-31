package com.uth.BE.DAO;

import com.uth.BE.Pojo.Notification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class NotificationDAO {
    private final SessionFactory sessionFactory;

    public NotificationDAO(String persistenceUnitName) {
        Configuration config = new Configuration().configure(persistenceUnitName);
        sessionFactory = config.buildSessionFactory();
    }

    //SAVE
    public void save(Notification notification) {
        Session session = sessionFactory.openSession();
        Transaction t = null;
        try {
            t = session.beginTransaction();
            session.save(notification);
            t.commit();
            System.out.println("Comment saved");
        } catch (Exception e) {
            if (t != null) {
                t.rollback();
            }
            System.out.println("Error: " + e.getMessage());
        } finally {
            sessionFactory.close();
            session.close();
        }
    }

    //UPDATE
    public void update(Notification notification) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            session.update(notification);
            t.commit();
            System.out.println("Comment updated");
        } catch (Exception e) {
            t.rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            sessionFactory.close();
            session.close();
        }
    }

    //GET
    public List<Notification> getComments() {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        List<Notification> notiList = null;
        try {
            notiList = session.createQuery("from Comment", Notification.class).getResultList();
            t.commit();
        } catch (Exception e) {
            t.rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            sessionFactory.close();
            session.close();
        }
        return notiList;
    }

    //FIND
    public Notification findById(int notification_id) {
        try (Session session = sessionFactory.openSession()) {
            return (Notification) session.get(Notification.class, notification_id);
        }
    }

    //DELETE
    public void deleteById(int notification_id) {
        Session session = sessionFactory.openSession();
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Notification notification = (Notification) session.get(Notification.class, notification_id);
            session.delete(notification);
            t.commit();
        } catch (RuntimeException e) {
            t.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
