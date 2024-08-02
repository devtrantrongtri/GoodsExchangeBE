package com.uth.BE.Service;

import com.uth.BE.Pojo.User;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while creating user: " + e.getMessage());
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
}