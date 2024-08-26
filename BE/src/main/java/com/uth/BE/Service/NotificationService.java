package com.uth.BE.Service;

import com.uth.BE.Entity.Comment;
import com.uth.BE.Entity.Notification;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.CommentRepository;
import com.uth.BE.Repository.NotificationRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Notification save(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications", savedNotification);
        return savedNotification;
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findById(int id) {
        return  notificationRepository.findById(id);
    }

    @Override
    public void delete(int id){
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification update(Notification notification) {
        Notification exiting = notificationRepository.findById(notification.getNotificationId()).orElse(null);
        exiting.setMessage(notification.getMessage());
        exiting.setRead(notification.isRead());
        exiting.setCreatedAt(new Timestamp(notification.getCreatedAt().getTime()));
        return notificationRepository.save(exiting);
    }

    @Override
    public void markRead(int notificationID) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationID);
        notificationOpt.ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    public List<Notification> getNotificationByUser(int userID) {
        User u = userRepository.findById(userID).orElse(null);
        if (u != null) {
            return notificationRepository.findByUsers(u);
        } else {
            return List.of();
        }
    }

    @Override
    public List<Notification> getNotificationByUsername(String username) {
        User u = userRepository.findByUsername(username).orElse(null);
        if (u != null) {
            return notificationRepository.findByUsers(u);
        } else {
            return List.of();
        }
    }

    @Override
    public List<Notification> findCommentWithSort(String field, String order) {
        if (ReflectionUtils.findField(Comment.class, field) == null) {
            throw new IllegalArgumentException("Invalid field name: " + field);
        }

        return notificationRepository.findAll(Sort.by(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, field));
    }

    @Override
    public Page<Notification> findCommentWithPage(int offset, int size) {
        return notificationRepository.findAll(PageRequest.of(offset,size));
    }

    @Override
    public Page<Notification> findCommentWithPageAndSort(int offset, int size, String field, String order) {
        if (ReflectionUtils.findField(Comment.class, field) == null) {
            throw new IllegalArgumentException("Invalid field name: " + field);
        }

        return notificationRepository.findAll(PageRequest.of(offset,size).withSort(Sort.by(order.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, field)));
    }
}
