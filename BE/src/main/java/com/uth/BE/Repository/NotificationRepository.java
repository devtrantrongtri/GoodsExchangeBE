package com.uth.BE.Repository;

import com.uth.BE.Pojo.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
