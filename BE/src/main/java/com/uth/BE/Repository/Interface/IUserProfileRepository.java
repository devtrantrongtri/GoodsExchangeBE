package com.uth.BE.Repository.Interface;

import com.uth.BE.Pojo.UserProfile;

import java.util.List;

public interface IUserProfileRepository {
    public UserProfile getUserProfileById(int id);
    public UserProfile getUserProfileByUsername(String username);
    public List<UserProfile> getAllUserProfileById(int id);
    public void addUserProfile(UserProfile userProfile);
    public void updateUserProfile(UserProfile userProfile);
    public void deleteUserProfile(int id);
}
