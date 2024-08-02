package com.uth.BE.Service.Interface;

import com.uth.BE.Pojo.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> getALLUser();
    public Optional<User> getUserById(int id);
    public void createUser(User user);
    public void deleteUserById(int id);
    public void updateUser(User user);
}
