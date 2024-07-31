package com.uth.BE.Pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id")
    private int notification_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User users ;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_read")
    private boolean is_read = false;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "create_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Timestamp created_at;

    public Notification() {
        super();
    }

    public Notification(String message) {
        super();
        this.message = message;
    }
}
