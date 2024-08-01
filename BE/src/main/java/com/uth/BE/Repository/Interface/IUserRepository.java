package com.uth.BE.Repository.Interface;

import com.uth.BE.Pojo.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    public List<User> findAll();
    public Optional<User> findById(int id);
    public void save(User user);
    public void deleteById(int id);
    public void update(User user);
}
