package com.uth.BE.Controller;

import com.uth.BE.Pojo.Notification;
import com.uth.BE.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/createNoti")
    public Notification createNoti(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }

    @GetMapping("/getAllNoti")
    public List<Notification> getAllNoti() {
        return notificationService.getAll();
    }

    @GetMapping("/getNotiByID/{id}")
    public Optional<Notification> getNotiByID(int id) {
        return notificationService.findById(id);
    }

    @PostMapping("/updateNoti")
    public Notification updateNoti(Notification notification) {
        return notificationService.update(notification);
    }

    @DeleteMapping("/deleteNoti")
    public void deleteNoti(int id) {
        notificationService.delete(id);
    }
}
