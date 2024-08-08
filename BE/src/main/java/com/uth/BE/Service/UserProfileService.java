package com.uth.BE.Service;

import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Repository.UserProfileRepository;
import com.uth.BE.Service.Interface.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService implements IUserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfile> getUserProfileById(int id) {
        try {
            return userProfileRepository.findById(id);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while fetching user profile by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<UserProfile> getAllUserProfile() {
        try {
            return userProfileRepository.findAll();
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while fetching all user profiles: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void addUserProfile(UserProfile userProfile) {
        try {
            userProfileRepository.save(userProfile);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while adding user profile: " + e.getMessage());
        }
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        try {
            userProfileRepository.save(userProfile);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while updating user profile: " + e.getMessage());
        }
    }

    @Override
    public void deleteUserProfile(int id) {
        try {
            userProfileRepository.deleteById(id);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while deleting user profile: " + e.getMessage());
        }
    }
}