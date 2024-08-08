package com.uth.BE.Controller;

import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Service.Interface.IUserProfileService;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public GlobalRes<List<UserProfile>> getAllUserProfiles() {
        List<UserProfile> profiles = userProfileService.getAllUserProfile();
        if (profiles != null && !profiles.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "UserProfiles found", profiles);
        } else {
            return new GlobalRes<>(HttpStatus.NO_CONTENT, "No UserProfiles found", null);
        }
    }

    @GetMapping("/{id}")
    public GlobalRes<Optional<UserProfile>> getUserProfileById(@PathVariable int id) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(id);
        if (userProfile.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "UserProfile found", userProfile);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "UserProfile not found", Optional.empty());
        }
    }

    @PostMapping
    public GlobalRes<String> addUserProfile(@RequestBody UserProfile userProfile) {
        try {
            userProfileService.addUserProfile(userProfile);
            return new GlobalRes<>(HttpStatus.CREATED, "UserProfile created successfully", null);
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create UserProfile", null);
        }
    }

    @PutMapping("/{id}")
    public GlobalRes<String> updateUserProfile(@PathVariable int id, @RequestBody UserProfile userProfile) {
        try {
            Optional<UserProfile> existingUserProfile = userProfileService.getUserProfileById(id);
            if (existingUserProfile.isPresent()) {
                userProfile.setProfileId(id);
                userProfileService.updateUserProfile(userProfile);
                return new GlobalRes<>(HttpStatus.OK, "UserProfile updated successfully", null);
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "UserProfile not found", null);
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update UserProfile", null);
        }
    }

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
