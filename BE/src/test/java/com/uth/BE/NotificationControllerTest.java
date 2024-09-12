package com.uth.BE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.NotificationController;
import com.uth.BE.Entity.Notification;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.NotificationService;
import com.uth.BE.Service.UserService;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.req.NotificationReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(NotificationController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserService userService;

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
    public void testCreateNotification_Success() throws Exception {
        NotificationReq notificationReq = new NotificationReq();
        notificationReq.setMessage("Test Notification");
        notificationReq.setUserId(1);

        Notification notification = new Notification("Test Notification");
        when(notificationService.save(any(Notification.class))).thenReturn(notification);
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/notifications")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(notificationReq))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("Successfully create this notification"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetAllNotifications_Success() throws Exception {
        List<Notification> notificationList = Arrays.asList(
                new Notification("Notification 1"),
                new Notification("Notification 2")
        );

        when(notificationService.getAll()).thenReturn(notificationList);

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully get all notification"))
                .andExpect(jsonPath("$.data[0].message").value("Notification 1"))
                .andExpect(jsonPath("$.data[1].message").value("Notification 2"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetNotificationById_Success() throws Exception {
        Notification notification = new Notification("Test Notification");

        when(notificationService.findById(anyInt())).thenReturn(Optional.of(notification));

        mockMvc.perform(get("/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully get this notification"))
                .andExpect(jsonPath("$.data.message").value("Test Notification"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetNotificationByUser_Success() throws Exception {
        List<Notification> notificationList = Arrays.asList(
                new Notification("Notification 1"),
                new Notification("Notification 2")
        );

        when(notificationService.getNotificationByUser(anyInt())).thenReturn(notificationList);

        mockMvc.perform(get("/notifications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("All notifications read successfully"))
                .andExpect(jsonPath("$.data[0].message").value("Notification 1"))
                .andExpect(jsonPath("$.data[1].message").value("Notification 2"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testDeleteNotification_Success() throws Exception {
        when(notificationService.findById(anyInt())).thenReturn(Optional.of(new Notification()));

        mockMvc.perform(delete("/notifications/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully delete this notification"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testUpdateNotification_Success() throws Exception {
        NotificationReq notificationReq = new NotificationReq();
        notificationReq.setMessage("Updated Notification");
        notificationReq.setUserId(1);

        Notification existingNotification = new Notification("Old Notification");

        when(notificationService.findById(anyInt())).thenReturn(Optional.of(existingNotification));
        when(notificationService.update(any(Notification.class))).thenReturn(existingNotification);

        mockMvc.perform(put("/notifications/update/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(notificationReq))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully updated the notification"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetNotificationsWithSort_Success() throws Exception {
        List<Notification> notificationList = Arrays.asList(
                new Notification("Notification 1"),
                new Notification("Notification 2")
        );

        when(notificationService.findCommentWithSort(anyString(), anyString())).thenReturn(notificationList);

        mockMvc.perform(get("/notifications/sort/field/asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("All comments read successfully"))
                .andExpect(jsonPath("$.data[0].message").value("Notification 1"))
                .andExpect(jsonPath("$.data[1].message").value("Notification 2"))
                .andDo(print());
    }


}
