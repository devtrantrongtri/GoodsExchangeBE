package com.uth.BE.Service;

import com.uth.BE.Pojo.UserProfile;
import com.uth.BE.Repository.UserProfileRepository;
import com.uth.BE.Service.Interface.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService  implements IUserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Optional<UserProfile> getUserProfileById(int id) {
        return userProfileRepository.getUserProfileById(id);
    }

    @Override
    public List<UserProfile> getAllUserProfileById(int id) {
        return userProfileRepository.getAllUserProfileById(id);
    }

    @Override
    public void addUserProfile(UserProfile userProfile) {
         userProfileRepository.addUserProfile(userProfile);
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        userProfileRepository.updateUserProfile(userProfile);
    }

    @Override
    public void deleteUserProfile(int id) {
        userProfileRepository.deleteUserProfile(id);
    }
}
