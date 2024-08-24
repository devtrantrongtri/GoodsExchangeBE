package com.uth.BE.Service;

import com.uth.BE.Entity.User;
import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Repository.UserProfileRepository;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.dto.req.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;
    private static final List<String> VALID_ROLES = Arrays.asList("ADMIN", "MODERATOR", "CLIENT");

    // Check if roles are valid
    private boolean areRolesValid(String roles) {
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .allMatch(VALID_ROLES::contains);
    }
    @Autowired
    public UserService(UserRepository userRepository,UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<User> getALLUser() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while fetching all users: " + e.getMessage());
            return null; // Chỗ này sẽ update thành một kiểu trả về. sẽ update sau.
        }
    }

    @Override
    public Optional<User> getUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while fetching user by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<UserDTO> findAllUserSent(Integer userId) {
        // Lấy danh sách User từ repository
        List<User> users = userRepository.findUsersWithMessages(userId);

        // Chuyển đổi danh sách User sang UserDTO
        List<UserDTO> userDTOs = users.stream()
                .map(UserDTO::convertToDTO)  // Chuyển đổi từng User sang UserDTO
                .collect(Collectors.toList());

        return userDTOs;
    }



    @Override
    public void createUser(User user) {
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                // Handle the case when the username and email already exists
                throw new IllegalArgumentException("Username already exists");
            } else if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            UserProfile userProfile = new UserProfile();
            userProfile.setUser(user); // Link UserProfile with the User
            userProfileRepository.save(userProfile);
        } catch (IllegalArgumentException e) {

            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {

            System.err.println("Error occurred while creating user: " + e.getMessage());
            throw new RuntimeException("Failed to create user", e);
        }
    }
    @Override
    public void deleteUserById(int id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            // Log the exception and handle it as necessary
            System.err.println("Error occurred while deleting user: " + e.getMessage());
        }
    }

    @Override
    public User updateUser(Integer userId, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(userId);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            if (updatedUser.getUsername() != null && !existingUser.getUsername().equals(updatedUser.getUsername())) {
                if (userRepository.existsByUsername(updatedUser.getUsername())) {
                    throw new IllegalArgumentException("Username already exists");
                }
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null && !existingUser.getEmail().equals(updatedUser.getEmail())) {
                if (userRepository.existsByEmail(updatedUser.getEmail())) {
                    throw new IllegalArgumentException("Email already exists");
                }
                existingUser.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            if (updatedUser.getAddress() != null) {
                existingUser.setAddress(updatedUser.getAddress());
            }
            // Validate and update roles
            if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
                if (areRolesValid(updatedUser.getRoles())) {
                    existingUser.setRoles(updatedUser.getRoles());
                } else {
                    throw new IllegalArgumentException("Invalid roles provided");
                }
            }
            if (updatedUser.getCreatedAt() != null) {
                existingUser.setCreatedAt(updatedUser.getCreatedAt());
            }
            return userRepository.save(existingUser);
        } else {
            throw new NoSuchElementException("User not found");
        }
    }


    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching user by username: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteAllUser() {
        userRepository.deleteAll();
    }

    // sort User theo thứ tự tăng hoặc giảm bởi field.
    @Override
    public List<User> getALLUserWithSort(String field, String order) {
        // asc -> tăng, desc -> down
        Sort.Direction sortDirection = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        // check filed is correctly
        Field userField = ReflectionUtils.findField(User.class, field);

        if (userField == null) {
            throw new IllegalArgumentException("Field '" + field + "' does not exist in User class");
//             return userRepository.findAll();
        }
        // Tạo đối tượng Sort với filed  và order
        Sort sort = Sort.by(sortDirection, field);

        return userRepository.findAll(sort);
    }

    @Override
    public Page<User> getAllUsersWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties) {
        Sort.Direction directed = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, directed, properties);
        return userRepository.findAll(pageable);
    }

}