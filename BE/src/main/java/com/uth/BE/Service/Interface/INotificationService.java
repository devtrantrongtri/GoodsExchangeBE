package com.uth.BE.Service.Interface;

import com.uth.BE.Pojo.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    public Notification save(Notification comment);

    public List<Notification> getAll();

    public Optional<Notification> findById(int id);

    public void delete(int id);
}
