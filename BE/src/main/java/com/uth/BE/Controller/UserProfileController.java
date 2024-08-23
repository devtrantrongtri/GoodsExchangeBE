package com.uth.BE.Controller;

import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Entity.model.CustomUserDetails;
import com.uth.BE.Service.Interface.IUserProfileService;
import com.uth.BE.dto.res.GlobalRes;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userProfiles")
public class UserProfileController {

    private final IUserProfileService userProfileService;

    @Autowired
    public UserProfileController(IUserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping
    public GlobalRes<List<UserProfile>> getAllUserProfiles() {
        List<UserProfile> profiles = userProfileService.getAllUserProfile();
        if (profiles != null && !profiles.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "UserProfiles found", profiles);
        } else {
            return new GlobalRes<>(HttpStatus.NO_CONTENT, "No UserProfiles found", null);
        }
    }

    // get user profile by admin
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @GetMapping("/{id}")
    public GlobalRes<Optional<UserProfile>> getUserProfileById(@PathVariable int id) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(id);
        if (userProfile.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "UserProfile found", userProfile);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "UserProfile not found", Optional.empty());
        }
    }

    // only get for their-self profile
    @GetMapping("/userProfile")
    public GlobalRes<Optional<UserProfile>> getUserProfileTheir() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            Integer userId = customUserDetails.getUserId();
            Optional<UserProfile> userProfile = userProfileService.getUserProfileById(userId);
            if (userProfile.isPresent()) {
                return new GlobalRes<>(HttpStatus.OK, "UserProfile found", userProfile);
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "UserProfile not found", Optional.empty());
            }
        } else {
            return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
    @PostMapping
    public GlobalRes<String> addUserProfile(@RequestBody UserProfile userProfile) {
        try {
            // thêm UserProfile
            userProfileService.addUserProfile(userProfile);
            return new GlobalRes<>(HttpStatus.CREATED, "UserProfile created successfully", null);
        } catch (EntityNotFoundException e) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Failed to create UserProfile: " + e.getMessage(), null);
        } catch (RuntimeException e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create UserProfile: " + e.getMessage(), null);
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage(), null);
        }
    }


    @PutMapping("/update")
    public GlobalRes<String> updateUserProfile(@RequestBody UserProfile updatedUserProfile) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            Integer userId = customUserDetails.getUserId();
            try {
                Optional<UserProfile> existingUserProfileOpt = userProfileService.getUserProfileById(userId);
                if (existingUserProfileOpt.isPresent()) {
                    UserProfile existingUserProfile = existingUserProfileOpt.get();

                    // Update fields as necessary
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

                    userProfileService.updateUserProfile(existingUserProfile);
                    return new GlobalRes<>(HttpStatus.OK, "UserProfile updated successfully", null);
                } else {
                    return new GlobalRes<>(HttpStatus.NOT_FOUND, "UserProfile not found", null);
                }
            } catch (Exception e) {
                return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update UserProfile", null);
            }
        } else {
            return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
    @DeleteMapping("/{id}")
    public GlobalRes<String> deleteUserProfile(@PathVariable int id) {
        try {
            Optional<UserProfile> existingUserProfile = userProfileService.getUserProfileById(id);
            if (existingUserProfile.isPresent()) {
                userProfileService.deleteUserProfile(id);
                return new GlobalRes<>(HttpStatus.OK, "UserProfile deleted successfully", null);
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "UserProfile not found", null);
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete UserProfile", null);
        }
    }
}
