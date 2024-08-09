package com.uth.BE.Controller;

import com.uth.BE.Entity.Comment;
import com.uth.BE.Entity.Notification;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.NotificationService;
import com.uth.BE.Service.UserService;
import com.uth.BE.dto.req.NotificationReq;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @PostMapping()
    public GlobalRes<NotificationReq> createNotification(@RequestBody NotificationReq notificationReq) {
        Notification notification = new Notification(notificationReq.getMessage());
        Optional<User> user = userService.getUserById(notificationReq.getUserId());
        notification.setUser(user.orElse(null));

        Notification savedNotification = notificationService.save(notification);

        if (savedNotification != null) {
            return new GlobalRes<>(HttpStatus.CREATED, "Successfully create this notification", null);
        }

        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed create this notification", null);
    }

    @GetMapping()
    public GlobalRes<List<Notification>> getAllNotification() {
        List<Notification> notificationList = notificationService.getAll();
        if (!notificationList.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get all notification", notificationList);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get all notification", null);
    }

    @GetMapping("/{id}")
    public GlobalRes<Optional<Notification>> getNotificationByID(@PathVariable("id") int id) {
        Optional<Notification> notificationOptional = notificationService.findById(id);
        if (notificationOptional != null) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get this notification", notificationOptional);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get this notification", null);
    }

    @GetMapping("/user/{id}")
    public GlobalRes<List<Notification>> getNotificationByUser(@PathVariable("id") int userID) {
        List<Notification> notificationList = notificationService.getNotificationByUser(userID);
        if (notificationList.isEmpty() || notificationList == null) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "List empty",null);
        }
        return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", notificationList);
    }

    @GetMapping("/username/{username}")
    public GlobalRes<List<Notification>> getNotificationByUsername(@PathVariable("username") String username) {
        List<Notification> notificationList = notificationService.getNotificationByUsername(username);
        if (notificationList.isEmpty() || notificationList == null) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "List empty",null);
        }
        return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", notificationList);
    }

    @PostMapping("/update")
    public GlobalRes<Notification> updateNotification(Notification notification) {
        Notification c = notificationService.update(notification);
        if(c != null){
            return new GlobalRes<>(HttpStatus.OK, "Successfully update this notification", null);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update this notification", null);
    }

    @DeleteMapping("/{id}")
    public GlobalRes<Notification> deleteNotification(@PathVariable("id") int id) {
        try {
            notificationService.delete(id);
            return new GlobalRes<>(HttpStatus.OK, "Successfully delete this notification", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to delete this notification", null);
        }
    }

    @PutMapping("/{id}")
    public GlobalRes<Notification> markReadNotification(@PathVariable("id") int id) {
        try {
            notificationService.markRead(id);
            return new GlobalRes<>(HttpStatus.OK, "Successfully Set Read", null);
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed Set Read", null);
        }
    }
}
