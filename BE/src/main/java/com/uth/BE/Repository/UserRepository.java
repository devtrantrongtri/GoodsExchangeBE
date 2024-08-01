package com.uth.BE.Repository;

import com.uth.BE.DAO.UserDAO;
import com.uth.BE.Pojo.User;
import com.uth.BE.Repository.Interface.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository  implements IUserRepository {
    private final UserDAO userDAO ;

    @Autowired
    public UserRepository(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public void deleteById(int id) {
        userDAO.deleteById(id);
    }

    @Override
    public void update(User user) {
        if (userDAO.existsById(user.getUserId())) {
            userDAO.save(user);
        } else {
//            throw new IllegalStateException("Cannot update non-existing user.");
            System.out.println("User Not Found");
        }
    }
}
