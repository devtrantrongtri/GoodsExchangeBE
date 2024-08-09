package com.uth.BE.Repository;

import com.uth.BE.Entity.Notification;
import com.uth.BE.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUser(User user);
}
