package com.uth.BE.Service;

import com.uth.BE.Entity.User;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getALLUser() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while fetching all users: " + e.getMessage());
            return null; // Chỗ này sẽ update thành một kiểu trả về. sẽ update sau.
        }
    }

    @Override
    public Optional<User> getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while fetching user by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllUserSent(Integer userid) {
        return userRepository.findUsersWithMessages(userid);
    }

    @Override
    public void createUser(User user) {
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                // Handle the case when the username and email already exists
                throw new IllegalArgumentException("Username already exists");
            } else if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10 );
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (IllegalArgumentException e) {

            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {

            System.err.println("Error occurred while creating user: " + e.getMessage());
            throw new RuntimeException("Failed to create user", e);
        }
    }
    @Override
    public void deleteUserById(int id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while deleting user: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while updating user: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching user by username: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteAllUser() {
        userRepository.deleteAll();
    }
}