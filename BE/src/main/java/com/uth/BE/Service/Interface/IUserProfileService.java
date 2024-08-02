package com.uth.BE.Service.Interface;

import com.uth.BE.Pojo.UserProfile;

import java.util.List;
import java.util.Optional;

public interface IUserProfileService {
    public Optional<UserProfile> getUserProfileById(int id);
    public List<UserProfile> getAllUserProfileById(int id);
    public void addUserProfile(UserProfile userProfile);
    public void updateUserProfile(UserProfile userProfile);
    public void deleteUserProfile(int id);
}
