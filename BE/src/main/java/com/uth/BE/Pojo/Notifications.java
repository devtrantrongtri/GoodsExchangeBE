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
import java.util.Set;
import java.util.HashSet;
import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id")
    private int notification_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<User> users = new HashSet<User>();

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_read")
    private boolean is_read = false;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "create_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Timestamp created_at;

    public Notifications() {
        super();
    }

    public Notifications(String message) {
        super();
        this.message = message;
    }
}
