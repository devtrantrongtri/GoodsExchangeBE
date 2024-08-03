package com.uth.BE.Controller;

import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getALLUser();
        if (users != null && !users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                user.setUserId(existingUser.get().getUserId()); // get all, get detail

                userService.updateUser(user);
                return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                userService.deleteUserById(id);
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
