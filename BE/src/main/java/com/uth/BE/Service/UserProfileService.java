package com.uth.BE.Service;

import com.uth.BE.Entity.User;
import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Repository.UserProfileRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IUserProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserProfileService implements IUserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository,UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
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

    public void addUserProfile(UserProfile userProfile) {
        try {
            // Kiểm tra xem user có tồn tại không
            Optional<User> existingUser = userRepository.findById(userProfile.getUser().getUserId());
            if (existingUser.isPresent()) {
                userProfile.setUser(existingUser.get()); // Đảm bảo rằng User được lấy từ cơ sở dữ liệu
                userProfileRepository.save(userProfile);
            } else {
                throw new EntityNotFoundException("User with id " + userProfile.getUser().getUserId() + " not found");
            }
        } catch (EntityNotFoundException e) {
            System.err.println("Error occurred while adding user profile: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error occurred while adding user profile: " + e.getMessage());
            throw new RuntimeException("Failed to add user profile", e);
        }
    }

    @Override
    public void updateUserProfile(UserProfile updatedUserProfile) {
        Optional<UserProfile> existingUserProfileOpt = userProfileRepository.findById(updatedUserProfile.getProfileId());

        if (existingUserProfileOpt.isPresent()) {
            UserProfile existingUserProfile = existingUserProfileOpt.get();

            if (updatedUserProfile.getFirstName() != null) {
                existingUserProfile.setFirstName(updatedUserProfile.getFirstName());
            }
            if (updatedUserProfile.getLastName() != null) {
                existingUserProfile.setLastName(updatedUserProfile.getLastName());
            }
            if (updatedUserProfile.getBio() != null) {
                existingUserProfile.setBio(updatedUserProfile.getBio());
            }
            if (updatedUserProfile.getProfileImageUrl() != null) {
                existingUserProfile.setProfileImageUrl(updatedUserProfile.getProfileImageUrl());
            }
            userProfileRepository.save(existingUserProfile);
        } else {
            throw new NoSuchElementException("UserProfile not found");
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