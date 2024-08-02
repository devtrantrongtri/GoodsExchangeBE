package com.uth.BE.Service;

import com.uth.BE.Pojo.Notification;
import com.uth.BE.Repository.NotificationRepository;
import com.uth.BE.Service.Interface.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        super();
        this.repo = repo;
    }

    @Override
    public Notification save(Notification notification) {
        return repo.save(notification);
    }

    @Override
    public List<Notification> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Notification> findById(int id) {
        Optional<Notification> notification = Optional.ofNullable(repo.findById(id).orElse(null));
        return  notification;
    }

    @Override
    public void delete(int id){
        repo.deleteById(id);
    }

    @Override
    public Notification update(Notification notification) {
        Notification exiting = repo.findById(notification.getNotificationId()).orElse(null);
        exiting.setMessage(notification.getMessage());
        exiting.setRead(notification.isRead());
        exiting.setCreatedAt(new Timestamp(notification.getCreatedAt().getTime()));
        return repo.save(exiting);
    }
}
