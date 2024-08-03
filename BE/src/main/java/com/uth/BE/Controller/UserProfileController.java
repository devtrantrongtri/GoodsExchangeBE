package com.uth.BE.Controller;

import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Service.Interface.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserProfile>> getAllUserProfiles() {
        List<UserProfile> profiles = userProfileService.getAllUserProfile();
        if (profiles != null && !profiles.isEmpty()) {
            return new ResponseEntity<>(profiles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserProfile>> getUserProfileById(@PathVariable int id) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(id);
        if (userProfile.isPresent()) {
            return new ResponseEntity<>(userProfile, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> addUserProfile(@RequestBody UserProfile userProfile) {
        try {
            userProfileService.addUserProfile(userProfile);
            return new ResponseEntity<>("UserProfile created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create UserProfile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserProfile(@PathVariable int id, @RequestBody UserProfile userProfile) {
        try {
            Optional<UserProfile> existingUserProfile = userProfileService.getUserProfileById(id);
            if (existingUserProfile.isPresent()) {
                userProfile.setProfileId(id);
                userProfileService.updateUserProfile(userProfile);
                return new ResponseEntity<>("UserProfile updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("UserProfile not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update UserProfile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable int id) {
        try {
            Optional<UserProfile> existingUserProfile = userProfileService.getUserProfileById(id);
            if (existingUserProfile.isPresent()) {
                userProfileService.deleteUserProfile(id);
                return new ResponseEntity<>("UserProfile deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("UserProfile not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete UserProfile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
