package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getALLUser();
    Optional<User> getUserById(int id);
    void createUser(User user);
    void deleteUserById(int id);
    void updateUser(User user);
    Optional<User> findByUsername(String username);
    void deleteAllUser();
    List<User> findAllUserSent(Integer userid);

}
