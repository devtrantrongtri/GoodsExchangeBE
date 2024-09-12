package com.uth.BE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.UserProfileController;
import com.uth.BE.Entity.UserProfile;
import com.uth.BE.Service.Interface.IStoreService;
import com.uth.BE.Service.Interface.IUserProfileService;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.when;

@WebMvcTest(UserProfileController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserProfileService userProfileService;

    @MockBean
    private IStoreService storeService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MyUserDetailService myUserDetailService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: Get all user profiles
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllUserProfiles_Success() throws Exception {
        when(userProfileService.getAllUserProfile()).thenReturn(Arrays.asList(new UserProfile(), new UserProfile()));

        mockMvc.perform(get("/userProfiles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("UserProfiles found"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andDo(print());
    }

    // Test: Get user profile by ID
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUserProfileById_Success() throws Exception {
        when(userProfileService.getUserProfileById(anyInt())).thenReturn(Optional.of(new UserProfile()));

        mockMvc.perform(get("/userProfiles/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("UserProfile found"))
                .andDo(print());
    }

    // Test: Create a new user profile
    @Test
    @WithMockUser(username = "client", roles = {"CLIENT"})
    public void testAddUserProfile_Success() throws Exception {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        userProfile.setBio("This is a test bio.");

        String payload = objectMapper.writeValueAsString(userProfile);

        mockMvc.perform(post("/userProfiles")
                        .with(csrf())  // Ensure CSRF token is present
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("UserProfile created successfully"))
                .andDo(print());
    }


    // Test: Delete user profile
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUserProfile_Success() throws Exception {
        when(userProfileService.getUserProfileById(anyInt())).thenReturn(Optional.of(new UserProfile()));

        mockMvc.perform(delete("/userProfiles/1")
                        .with(csrf())  // Ensure CSRF token is present
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("UserProfile deleted successfully"))
                .andDo(print());
    }

//    // Test: Upload profile image
//    @Test
//    @WithMockUser(username = "client", roles = {"CLIENT"})
//    public void testUploadProfileImage_Success() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("file", "profile-image.jpg", MediaType.IMAGE_JPEG_VALUE, "image content".getBytes());
//        when(userProfileService.getUserProfileById(anyInt())).thenReturn(Optional.of(new UserProfile()));
//        when(storeService.saveFile(any(), any())).thenReturn(true);
//
//        mockMvc.perform(multipart("/userProfiles/image").file(file))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.msg").value("Profile image uploaded successfully"))
//                .andDo(print());
//    }
}
