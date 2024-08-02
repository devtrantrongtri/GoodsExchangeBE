package com.uth.BE.Repository;

import com.uth.BE.DAO.UserProfileDAO;
import com.uth.BE.Pojo.UserProfile;
import com.uth.BE.Repository.Interface.IUserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserProfileRepository implements IUserProfileRepository {
    private final UserProfileDAO userProfileDAO;
    @Autowired
    public UserProfileRepository(UserProfileDAO userProfileDAO) {
        this.userProfileDAO = userProfileDAO;
    }

    @Override
    public Optional<UserProfile> getUserProfileById(int id) {
        return userProfileDAO.findById(id);
    }


    @Override
    public List<UserProfile> getAllUserProfileById(int id) {
        return userProfileDAO.
    }

    @Override
    public void addUserProfile(UserProfile userProfile) {

    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {

    }

    @Override
    public void deleteUserProfile(int id) {

    }
}
