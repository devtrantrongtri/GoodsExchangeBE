package com.uth.BE.DAO;

import com.uth.BE.Pojo.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class CommentDAO {
    private SessionFactory sessionFactory = null;
    private Configuration config = null;

    public CommentDAO(String persistenceUnitName) {
        config = new Configuration().configure(persistenceUnitName);
        sessionFactory = config.buildSessionFactory();
    }

    //SAVE
    public void save(Comment comment) {
    Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            session.save(comment);
            t.commit();
            System.out.println("Comment saved");
        } catch (Exception e) {
            t.rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            sessionFactory.close();
            session.close();
        }
    }

    //UPDATE
    public void update(Comment comment) {
    Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            session.update(comment);
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
    public List<Comment> getComments() {
      Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        List<Comment> commentList = null;
        try {
            commentList = session.createQuery("from Comment", Comment.class).getResultList();
            t.commit();
        } catch (Exception e) {
            t.rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            sessionFactory.close();
            session.close();
        }
        return commentList;
    }

    //FIND
    public Comment findById(int comment_id) {
       Session session = sessionFactory.openSession();
        try {
            return (Comment) session.get(Comment.class, comment_id);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            session.close();
        }
    }

    //DELETE
    public void deleteById(int comment_id) {
       Session session = sessionFactory.openSession();
        Transaction t = session.getTransaction();
        try {
            t.begin();
            Comment comment = (Comment) session.get(Comment.class, comment_id);
            session.delete(comment);
            t.commit();
        } catch (RuntimeException e) {
            t.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
