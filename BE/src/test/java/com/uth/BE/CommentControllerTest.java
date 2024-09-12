package com.uth.BE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.CommentController;
import com.uth.BE.Entity.Comment;
import com.uth.BE.Service.Interface.ICommentService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.Service.ProductService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CommentController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICommentService commentService;

    @MockBean
    private IUserService userService;

    @MockBean
    private ProductService productService;

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
    public void testGetAllComments_Success() throws Exception {
        List<Comment> comments = Arrays.asList(new Comment("Test comment 1"), new Comment("Test comment 2"));

        when(commentService.findAll()).thenReturn(comments);

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully get all comment"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetCommentById_Success() throws Exception {
        Comment comment = new Comment("Test comment");

        when(commentService.findById(anyInt())).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully get this comment"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testDeleteComment_Success() throws Exception {
        when(commentService.findById(anyInt())).thenReturn(Optional.of(new Comment()));

        mockMvc.perform(delete("/comments/deleteComment/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully delete this comment"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetCommentByUser_Success() throws Exception {
        List<Comment> commentList = Arrays.asList(
                new Comment("Test comment 1"),
                new Comment("Test comment 2")
        );

        when(commentService.findCommentByUser(anyInt())).thenReturn(commentList);

        mockMvc.perform(get("/comments/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("All comments read successfully"))
                .andExpect(jsonPath("$.data[0].commentText").value("Test comment 1"))
                .andExpect(jsonPath("$.data[1].commentText").value("Test comment 2"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetCommentsByUserAndProduct_Success() throws Exception {
        List<Comment> commentList = Arrays.asList(
                new Comment("Test comment 1"),
                new Comment("Test comment 2")
        );

        when(commentService.findCommentByProductAndUser(anyInt(), anyInt())).thenReturn(commentList);

        mockMvc.perform(get("/comments/user/1/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully fetched comments for the user and product"))
                .andExpect(jsonPath("$.data[0].commentText").value("Test comment 1"))
                .andExpect(jsonPath("$.data[1].commentText").value("Test comment 2"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testCountCommentsByPost_Success() throws Exception {
        when(commentService.findCommentByProduct(anyInt())).thenReturn(Arrays.asList(new Comment(), new Comment()));

        mockMvc.perform(get("/comments/post/1/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully fetched comment count for the product"))
                .andExpect(jsonPath("$.data").value(2))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testSearchComments_Success() throws Exception {
        // Prepare the mock response
        List<Comment> commentList = Arrays.asList(
                new Comment("Comment containing keyword 1"),
                new Comment("Comment containing keyword 2")
        );

        when(commentService.searchCommentsByKeyword(anyString())).thenReturn(commentList);

        mockMvc.perform(get("/comments/search")
                        .param("keyword", "keyword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully fetched comments containing the keyword"))
                .andExpect(jsonPath("$.data[0].commentText").value("Comment containing keyword 1"))
                .andExpect(jsonPath("$.data[1].commentText").value("Comment containing keyword 2"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetCommentByPost_Success() throws Exception {
        List<Comment> commentList = Arrays.asList(
                new Comment("Test comment 1"),
                new Comment("Test comment 2")
        );

        when(commentService.findCommentByProduct(anyInt())).thenReturn(commentList);

        mockMvc.perform(get("/comments/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("All comments read successfully"))
                .andExpect(jsonPath("$.data[0].commentText").value("Test comment 1"))
                .andExpect(jsonPath("$.data[1].commentText").value("Test comment 2"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetCommentsWithSort_Success() throws Exception {
        List<Comment> commentList = Arrays.asList(
                new Comment("Test comment 1"),
                new Comment("Test comment 2")
        );

        when(commentService.findCommentWithSort(anyString(), anyString())).thenReturn(commentList);

        mockMvc.perform(get("/comments/sort/field/asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("All comments read successfully"))
                .andExpect(jsonPath("$.data[0].commentText").value("Test comment 1"))
                .andExpect(jsonPath("$.data[1].commentText").value("Test comment 2"))
                .andDo(print());
    }


}
