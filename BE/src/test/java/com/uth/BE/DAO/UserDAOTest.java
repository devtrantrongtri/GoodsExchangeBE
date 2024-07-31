package com.uth.BE.DAO;
import com.uth.BE.Pojo.User;
//import com.uth.BE.DAO.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        // Nếu cần thiết, thiết lập dữ liệu cho mỗi test
    }

    @AfterEach
    public void tearDown() {
        // Dọn dẹp dữ liệu sau mỗi test nếu cần
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");

        userDAO.save(user);
        assertNotNull(userDAO.findById(user.getUserId()));
    }

    @Test
    public void testGetUsers() {
        List<User> users = userDAO.getUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setUsername("testuser2");
        user.setPassword("password456");
        user.setEmail("testuser2@example.com");

        userDAO.save(user);
        User foundUser = userDAO.findById(user.getUserId());
        assertNotNull(foundUser);
        assertEquals("testuser2", foundUser.getUsername());
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("testuser3");
        user.setPassword("password789");
        user.setEmail("testuser3@example.com");

        userDAO.save(user);
        user.setUsername("updateduser");
        userDAO.update(user);

        User updatedUser = userDAO.findById(user.getUserId());
        assertNotNull(updatedUser);
        assertEquals("updateduser", updatedUser.getUsername());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("testuser4");
        user.setPassword("password000");
        user.setEmail("testuser4@example.com");

        userDAO.save(user);
        int userId = user.getUserId();
        userDAO.delete(userId);

        User deletedUser = userDAO.findById(userId);
        assertNull(deletedUser);
    }
}
