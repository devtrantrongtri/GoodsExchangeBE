package com.uth.BE.Controller;

import com.uth.BE.Entity.Notification;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.NotificationService;
import com.uth.BE.Service.UserService;
import com.uth.BE.dto.req.NotificationReq;
import com.uth.BE.dto.req.PaginationRequest;
import com.uth.BE.dto.res.GlobalRes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    //    @Autowired
    private final NotificationService notificationService;
    //    @Autowired
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('ADMIN')")
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
        return new GlobalRes<>(HttpStatus.OK, "All notifications read successfully", notificationList);
    }

    @GetMapping("/username/{username}")
    public GlobalRes<List<Notification>> getNotificationByUsername(@PathVariable("username") String username) {
        List<Notification> notificationList = notificationService.getNotificationByUsername(username);
        if (notificationList.isEmpty() || notificationList == null) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "List empty",null);
        }
        return new GlobalRes<>(HttpStatus.OK, "All notifications read successfully", notificationList);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public GlobalRes<Notification> updateNotification(@PathVariable("id") int notificationId, @RequestBody NotificationReq notificationReq) {
        Optional<Notification> existingNotification = notificationService.findById(notificationId);

        if (existingNotification.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Notification not found", null);
        }

        Notification updatedNotification = existingNotification.get();
        updatedNotification.setMessage(notificationReq.getMessage());
        updatedNotification.setUser(userService.getUserById(notificationReq.getUserId()).orElse(null));

        Notification savedNotification = notificationService.update(updatedNotification);

        if (savedNotification != null) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully updated the notification", savedNotification);
        }

        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update the notification", null);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
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

    @GetMapping("/sort/{field}/{order}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    private GlobalRes<List<Notification>> getCommentsWithSort(@PathVariable("field") String field, @PathVariable("order") String order) {
        List<Notification> notifications = notificationService.findCommentWithSort(field, order);
        if (notifications.isEmpty()) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No comments found", null);
        }
        return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", notifications);
    }

    @GetMapping("/page/{offset}/{size}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    private GlobalRes<Page<Notification>> getCommentsWithPage(@PathVariable("offset") int offset, @PathVariable("size") int size) {
        Page<Notification> notifications = notificationService.findCommentWithPage(offset,size);
        if (notifications.isEmpty()) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No comments found", null);
        }
        return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", notifications);
    }

    @GetMapping("/sortAndPage")
    @PreAuthorize("hasAnyRole('ADMIN')")
    private GlobalRes<Page<Notification>> getCommentsWithSortAndPage(@Valid @RequestBody PaginationRequest req) {
        try {
            Page<Notification> notificationPage = notificationService.findCommentWithPageAndSort(
                    req.getOffset(),
                    req.getPageSize(),
                    req.getField(),
                    req.getOrder()
            );
            if (notificationPage.getTotalElements() > 0) {
                return new GlobalRes<>(HttpStatus.OK, "All comments read successfully", notificationPage);
            }
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No comments found", null);
        } catch (IllegalArgumentException e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}
