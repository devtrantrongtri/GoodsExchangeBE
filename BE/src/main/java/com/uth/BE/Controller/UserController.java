package com.uth.BE.Controller;

import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.*;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;
    private final ICommentService commentService;
    private final IWishlistService wishlistService;
    private final INotificationService notificationService;
    private final IReportService reportService;
    private final IProductImgService productImgService;


@Autowired
    public UserController(IUserService userService, ICommentService commentService, IWishlistService wishlistService, INotificationService notificationService, IReportService reportService, IProductImgService productImgService) {
        this.userService = userService;
        this.commentService = commentService;
        this.wishlistService = wishlistService;
        this.notificationService = notificationService;
        this.reportService = reportService;
        this.productImgService = productImgService;
    }

    @GetMapping("/getAllUser")
    public GlobalRes<List<User>> getAllUsers() {
        List<User> users = userService.getALLUser();
        if (users != null && !users.isEmpty()) {
//            return new ResponseEntity<>(users, HttpStatus.OK);
            return new GlobalRes<List<User>>(HttpStatus.OK,"success",users);
        } else {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new GlobalRes<List<User>>(HttpStatus.NO_CONTENT,"success");
        }
    }

    @GetMapping("user/id/{id}")
    public GlobalRes<Optional<User>> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "User found", user);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", Optional.empty());
        }
    }

    @GetMapping("user/username/{userId}")
    public GlobalRes<List<User>> getListUserSentMessage(@PathVariable Integer userId) {
        List<User> users = userService.findAllUserSent(userId);
        if (users != null && !users.isEmpty()) {
//            return new ResponseEntity<>(users, HttpStatus.OK);
            return new GlobalRes<List<User>>(HttpStatus.OK,"success",users);
        } else {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new GlobalRes<List<User>>(HttpStatus.NO_CONTENT,"success");
        }
    }

    @GetMapping("user/username/{username}")
    public GlobalRes<Optional<User>> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "User found", user);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", Optional.empty());
        }
    }

    @PostMapping("/sign-up")
    public GlobalRes<String> createUser(@RequestBody User user) {
        try {

            userService.createUser(user);
            return new GlobalRes<>(HttpStatus.CREATED, "User created successfully");
        } catch (IllegalArgumentException e) {
            return new GlobalRes<>(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user");
        }
    }


    @PutMapping("user/{id}")
    public GlobalRes<String> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                user.setUserId(existingUser.get().getUserId());
                userService.updateUser(user);
                return new GlobalRes<>(HttpStatus.OK, "User updated successfully");
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user");
        }
    }


    @DeleteMapping("/admin/{id}")
    public GlobalRes<String> deleteUser(@PathVariable int id) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                userService.deleteUserById(id);
                return new GlobalRes<>(HttpStatus.OK, "User deleted successfully");
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user");
        }
    }

    @DeleteMapping("/admin")
    public GlobalRes<String> deleteAllUser() {
        try {
            userService.deleteAllUser();
            return new GlobalRes<String>( HttpStatus.OK,"success");
        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
            return new GlobalRes<String>( HttpStatus.INTERNAL_SERVER_ERROR,"failed");

        }
    }
}
