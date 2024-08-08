package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.UserProfile;

import java.util.List;
import java.util.Optional;

public interface IUserProfileService {
    public Optional<UserProfile> getUserProfileById(int id);
    public List<UserProfile> getAllUserProfile();
    public void addUserProfile(UserProfile userProfile);
    public void updateUserProfile(UserProfile userProfile);
    public void deleteUserProfile(int id);
}
