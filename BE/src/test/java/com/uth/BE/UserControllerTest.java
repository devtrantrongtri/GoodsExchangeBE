package com.uth.BE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.UserController;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.*;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.req.PaginationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private ICommentService commentService;

    @MockBean
    private IWishlistService wishlistService;

    @MockBean
    private INotificationService notificationService;

    @MockBean
    private IReportService reportService;

    @MockBean
    private IProductImgService productimgService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MyUserDetailService myUserDetailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetAllUsers_Success() throws Exception {
        List<User> userList = Arrays.asList(
                new User(1, "username1", "email1@example.com", "password", "CLIENT", "1234567890", "address", null),
                new User(2, "username2", "email2@example.com", "password", "CLIENT", "0987654321", "address", null)
        );

        when(userService.getALLUser()).thenReturn(userList);

        mockMvc.perform(get("/user/getAllUser")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.data.length()").value(userList.size()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetUserById_Success() throws Exception {
        User user = new User(1, "username1", "email1@example.com", "password", "CLIENT", "1234567890", "address", null);

        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/getUserById/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("User found"))
                .andExpect(jsonPath("$.data.username").value("username1"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testCreateUser_Success() throws Exception {
        User user = new User(1, "username1", "email1@example.com", "password", "CLIENT", "1234567890", "address", null);

        doNothing().when(userService).createUser(any(User.class));

        String payload = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/sign-up")
                        .with(csrf())  // Ensure CSRF token is present
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("User created successfully"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testDeleteUser_Success() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.of(new User()));

        doNothing().when(userService).deleteUserById(1);

        mockMvc.perform(delete("/user/delete/1")
                        .with(csrf())  // Ensure CSRF token is present
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("User deleted successfully"))
                .andDo(print());
    }

        @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetAllSortedUsers_Success() throws Exception {
        // Giả lập danh sách người dùng sắp xếp
        List<User> sortedUsers = Arrays.asList(
                new User(1, "username1", "email1@example.com", "password", "CLIENT", "1234567890", "address", null),
                new User(2, "username2", "email2@example.com", "password", "CLIENT", "0987654321", "address", null)
        );

        when(userService.getALLUserWithSort(anyString(), anyString())).thenReturn(sortedUsers);

        mockMvc.perform(get("/user/getAllSortedUsers/username/asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.data.length()").value(sortedUsers.size()))
                .andDo(print());
    }





}
