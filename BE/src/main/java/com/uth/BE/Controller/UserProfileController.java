package com.uth.BE.Controller;

import com.uth.BE.Entity.User;
import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Entity.model.CustomUserDetails;
import com.uth.BE.Service.Interface.IStoreService;
import com.uth.BE.Service.Interface.IUserProfileService;
import com.uth.BE.dto.res.GlobalRes;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userProfiles")
public class UserProfileController {

    private final IUserProfileService userProfileService;
    private final IStoreService storeService;

    @Autowired
    public UserProfileController(IUserProfileService userProfileService, IStoreService storeService) {
        this.userProfileService = userProfileService;
        this.storeService = storeService;
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


//    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR')")
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
    @PreAuthorize("hasAnyRole('CLIENT')")
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
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','MODERATOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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

    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','MODERATOR')")
    @PutMapping("/update")
    public GlobalRes<UserProfile> updateUserProfile(@RequestBody UserProfile updatedUserProfile) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            Integer userId = customUserDetails.getUserId();
            try {
                Optional<UserProfile> existingUserProfileOpt = userProfileService.getUserProfileById(userId);
                if (existingUserProfileOpt.isPresent()) {
                    UserProfile existingUserProfile = existingUserProfileOpt.get();
                    User existingUser = existingUserProfile.getUser();
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
                    // Cập nhật User
                    if (updatedUserProfile.getUser().getEmail() != null) {
                        existingUser.setEmail(updatedUserProfile.getUser().getEmail());
                    }
                    if (updatedUserProfile.getUser().getPhoneNumber() != null) {
                        existingUser.setPhoneNumber(updatedUserProfile.getUser().getPhoneNumber());
                    }
                    if (updatedUserProfile.getUser().getAddress() != null) {
                        existingUser.setAddress(updatedUserProfile.getUser().getAddress());
                    }
                    userProfileService.updateUserProfile(existingUserProfile);
                    return new GlobalRes<>(HttpStatus.OK, "UserProfile updated successfully", existingUserProfile);
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

    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','MODERATOR')")
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
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','MODERATOR')")
    @PostMapping("/image")
    public GlobalRes<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            Integer userId = customUserDetails.getUserId();
            UserProfile userProfile = userProfileService.getUserProfileById(userId)
                    .orElseThrow(() -> new RuntimeException("UserProfile not found"));

            String currentImage = userProfile.getProfileImageUrl();
            try {
                if (storeService.saveFile(file,currentImage)) {
                    UserProfile userProfiles = userProfileService.getUserProfileById(userId)
                            .orElseThrow(() -> new RuntimeException("UserProfile not found"));
                    userProfile.setProfileImageUrl(file.getOriginalFilename());
                    userProfileService.updateUserProfile(userProfiles);
                    return new GlobalRes<>(HttpStatus.OK, "Profile image uploaded successfully", null);
                } else {
                    return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload profile image", null);
                }
            } catch (Exception e) {
                return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload profile image: " + e.getMessage(), null);
            }
        } else {
            return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
//    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','MODERATOR')")
//    @PutMapping("/image")
//    public GlobalRes<String> updateProfileImage(@RequestParam("file") MultipartFile file) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof CustomUserDetails customUserDetails) {
//            Integer userId = customUserDetails.getUserId();
//            try {
//                // Create a unique filename
//                String originalFilename = file.getOriginalFilename();
//                String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
//                String uniqueFilename = userId + "_" + System.currentTimeMillis() + extension;
//
//                if (storeService.saveFile(file, uniqueFilename)) {
//                    UserProfile userProfile = userProfileService.getUserProfileById(userId)
//                            .orElseThrow(() -> new RuntimeException("UserProfile not found"));
//                    userProfile.setProfileImageUrl(file.getOriginalFilename());
//                    userProfileService.updateUserProfile(userProfile);
//                    return new GlobalRes<>(HttpStatus.OK, "Profile image updated successfully", null);
//                } else {
//                    return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update profile image", null);
//                }
//            } catch (Exception e) {
//                return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update profile image: " + e.getMessage(), null);
//            }
//        } else {
//            return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
//        }
//    }
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN','MODERATOR')")
    @DeleteMapping("/image")
    public GlobalRes<String> deleteProfileImage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            Integer userId = customUserDetails.getUserId();
            try {
                UserProfile userProfile = userProfileService.getUserProfileById(userId)
                        .orElseThrow(() -> new RuntimeException("UserProfile not found"));
                String filename = userProfile.getProfileImageUrl();
                if (filename != null && !filename.isEmpty()) {
                    if (storeService.deleteFile(filename)) {
                        userProfile.setProfileImageUrl(null);
                        userProfileService.updateUserProfile(userProfile);
                        return new GlobalRes<>(HttpStatus.OK, "Profile image deleted successfully", null);
                    } else {
                        return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete profile image", null);
                    }
                } else {
                    return new GlobalRes<>(HttpStatus.BAD_REQUEST, "No profile image to delete", null);
                }
            } catch (Exception e) {
                return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete profile image: " + e.getMessage(), null);
            }
        } else {
            return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @GetMapping("/image/{filename:.+}")
    public GlobalRes<byte[]> getProfileImage(@PathVariable String filename) {
        try {
            Resource file = storeService.loadFile(filename);
            if (file.exists() && file.isReadable()) {
                byte[] fileContent = Files.readAllBytes(file.getFile().toPath());
                return new GlobalRes<>(HttpStatus.OK, "File retrieved successfully", fileContent);
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "File not found");
            }
        } catch (IOException e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve file: " + e.getMessage(), null);
        }
    }
}
