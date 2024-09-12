package com.uth.BE.Controller;

import com.uth.BE.Entity.User;
import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Entity.model.CustomUserDetails;
import com.uth.BE.Service.Interface.*;
import com.uth.BE.dto.req.PaginationRequest;
import com.uth.BE.dto.req.UserDTO;
import com.uth.BE.dto.res.GlobalRes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;
    private final ICommentService commentService;
    private final IWishlistService wishlistService;
    private final INotificationService notificationService;
    private final IReportService reportService;
    private final IProductImgService productImgService;


@Autowired
    public UserController(IUserService userService, ICommentService commentService, IWishlistService wishlistService, INotificationService notificationService, IReportService reportService, IProductImgService productImgService) {
        this.userService = userService;
        this.commentService = commentService;
        this.wishlistService = wishlistService;
        this.notificationService = notificationService;
        this.reportService = reportService;
        this.productImgService = productImgService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/getAllUser")
    public GlobalRes<List<User>> getAllUsers() {
        List<User> users = userService.getALLUser();
        if (users != null && !users.isEmpty()) {
            return new GlobalRes<List<User>>(HttpStatus.OK,"success",users);
        } else {
            return new GlobalRes<List<User>>(HttpStatus.NO_CONTENT,"success");
        }
    }
    @GetMapping("/getUserById/{id}")
    public GlobalRes<Optional<User>> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "User found", user);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", Optional.empty());
        }
    }

    @GetMapping("/users/sent-messages")
    public GlobalRes<List<UserDTO>> getListUserSentMessage() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails customUserDetails) {
                Integer userId = customUserDetails.getUserId();
                List<UserDTO> users = userService.findAllUserSent(userId);
                if (users != null && !users.isEmpty()) {
                    return new GlobalRes<>(HttpStatus.OK, "success", users);
                } else {
                    return new GlobalRes<>(HttpStatus.NO_CONTENT, "No users found");
                }
            } else {
                return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
        } catch (Exception e) {

            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/usersProfile/sent-messages")
    public GlobalRes<List<Optional<UserProfile>>> getListUserProfileSentMessage() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails customUserDetails) {
                Integer userId = customUserDetails.getUserId();
                List<Optional<UserProfile>> users = userService.findAllUserProfileSent(userId);
                if (users != null && !users.isEmpty()) {
                    return new GlobalRes<>(HttpStatus.OK, "success", users);
                } else {
                    return new GlobalRes<>(HttpStatus.NO_CONTENT, "No users found");
                }
            } else {
                return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
        } catch (Exception e) {

            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/username/{username}")
    public GlobalRes<UserDTO> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            UserDTO userDTO = UserDTO.convertToDTO(user.get());
            return new GlobalRes<>(HttpStatus.OK, "User found", userDTO);
        } else {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", null);
        }
    }


    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalRes<String> createUser(@RequestBody User user) {
        try {

            userService.createUser(user);
            return new GlobalRes<>(HttpStatus.CREATED, "User created successfully");
        } catch (IllegalArgumentException e) {
            return new GlobalRes<>(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user");
        }
    }


    @PutMapping("/update")
    public GlobalRes<User> updateUser(@RequestBody User updatedUser) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            Integer userId = customUserDetails.getUserId();
            try {
                User user = userService.updateUser(userId, updatedUser);
                return new GlobalRes<>(HttpStatus.OK, "User updated successfully", user);
            } catch (IllegalArgumentException e) {
                return new GlobalRes<>(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (NoSuchElementException e) {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, e.getMessage());
            } catch (Exception e) {
                return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user");
            }
        } else {
            return new GlobalRes<>(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public GlobalRes<String> deleteUser(@PathVariable int id) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (existingUser.isPresent()) {
                userService.deleteUserById(id);
                return new GlobalRes<>(HttpStatus.OK, "User deleted successfully");
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteAll")
    public GlobalRes<String> deleteAllUser() {
        try {
            userService.deleteAllUser();
            return new GlobalRes<String>( HttpStatus.OK,"success");
        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
            return new GlobalRes<String>( HttpStatus.INTERNAL_SERVER_ERROR,"failed");

        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/getAllSortedUsers/{field}/{order}")
    public GlobalRes<List<User>> getAllSortedUsers(@PathVariable String field,@PathVariable String order) {
        List<User> users = userService.getALLUserWithSort(field,order);
        if (users != null && !users.isEmpty()) {
            return new GlobalRes<List<User>>(HttpStatus.OK,"success",users);
        } else {
            return new GlobalRes<List<User>>(HttpStatus.NO_CONTENT,"success");
        }
    }
//totalPages lấy dùng cho lastPage
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/getAllUsersWithPaginationAndSort")
    public GlobalRes<Page<User>> getAllUsersWithPaginationAndSort(
            @Valid @RequestBody PaginationRequest request) {
        try {
            Page<User> usersPage = userService.getAllUsersWithPaginationAndSort(
                    request.getOffset(), request.getPageSize(), request.getOrder(), request.getField());
            if (usersPage.hasContent()) {
                return new GlobalRes<>(HttpStatus.OK.value(), "success", usersPage);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT.value(), "No users found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST.value(), "Invalid parameters: " + e.getMessage());
        }
    }

}
