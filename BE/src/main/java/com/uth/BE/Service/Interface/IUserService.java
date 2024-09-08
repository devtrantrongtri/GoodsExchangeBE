package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.User;
import com.uth.BE.dto.req.UserDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getALLUser();
    Optional<User> getUserById(int id);
    void createUser(User user);
    void deleteUserById(int id);
    User updateUser(Integer userId, User updatedUser);
    Optional<User> findByUsername(String username);
    void deleteAllUser();
    List<UserDTO> findAllUserSent(Integer userid);
    List<User> getALLUserWithSort(String field, String order);
    Page<User> getAllUsersWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties);
}
