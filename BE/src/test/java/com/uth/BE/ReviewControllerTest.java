//package com.uth.BE;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.uth.BE.Controller.ReviewController;
//import com.uth.BE.Entity.Product;
//import com.uth.BE.Entity.Review;
//import com.uth.BE.Entity.User;
//import com.uth.BE.Service.Interface.IProductService;
//import com.uth.BE.Service.Interface.IReviewService;
//import com.uth.BE.Service.Interface.IUserService;
//import com.uth.BE.Service.JwtService;
//import com.uth.BE.Service.MyUserDetailService;
//import com.uth.BE.dto.req.PaginationRequest;
//import com.uth.BE.dto.req.ReviewReq;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = ReviewController.class)
//@Import({JwtService.class, MyUserDetailService.class})
//public class ReviewControllerTest {
//
//    @MockBean
//    private JwtService jwtService;
//
//    @MockBean
//    private MyUserDetailService myUserDetailService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private IProductService productService;
//
//    @MockBean
//    private IUserService userService;
//
//    @MockBean
//    private IReviewService reviewService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
//    public void testCreateReview() throws Exception {
//
//        Product mockProduct = new Product();
//        mockProduct.setProduct_id(1);
//        User mockUser = new User();
//        mockUser.setUserId(1);
//
//        ReviewReq reviewReq = new ReviewReq();
//        reviewReq.setProductID(1);
//        reviewReq.setUserID(1);
//        reviewReq.setReviewText("Review text");
//        reviewReq.setRating(5);
//
//        when(productService.getProductById(1)).thenReturn(Optional.of(mockProduct));
//        when(userService.getUserById(1)).thenReturn(Optional.of(mockUser));
//        when(reviewService.save(Mockito.any(Review.class))).thenReturn(new Review());
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/review")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(reviewReq)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.code").value(201))
//                .andExpect(jsonPath("$.msg").value("Review created successfully"))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void findAllReviews() throws Exception {
//        // Mock review list
//        Review mockReview1 = new Review("Great product", 5);
//        Review mockReview2 = new Review("Not bad", 3);
//        List<Review> mockReviews = Arrays.asList(mockReview1, mockReview2);
//
//        // Mock the service response
//        Mockito.when(reviewService.findAll()).thenReturn(mockReviews);
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reviews")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))  // Check code
//                .andExpect(jsonPath("$.msg").value("Successfully get all reviews"))  // Check message
//                .andExpect(jsonPath("$.data[0].reviewText").value("Great product"))  // Check review text
//                .andExpect(jsonPath("$.data[0].rating").value(5))  // Check review rating
//                .andExpect(jsonPath("$.data[1].reviewText").value("Not bad"))  // Check review text
//                .andExpect(jsonPath("$.data[1].rating").value(3))  // Check review rating
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void findReviewById_UserAuthenticated_ReviewFound() throws Exception {
//        // Mock review
//        Review mockReview = new Review("Great product", 5);
//        mockReview.setReview_id(1);  // Set ID to match the path variable
//
//        // Mock service response
//        Mockito.when(reviewService.findById(1)).thenReturn(Optional.of(mockReview));
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))  // Check code
//                .andExpect(jsonPath("$.msg").value("Successfully get this review"))  // Check message
//                .andExpect(jsonPath("$.data.reviewText").value("Great product"))  // Check review text
//                .andExpect(jsonPath("$.data.rating").value(5))  // Check review rating
//                .andDo(print());
//    }
//
//    @Test
//    public void getReviewByUser() throws Exception {
//        // Create mock reviews for a user
//        Review mockReview1 = new Review("Great product", 5);
//        Review mockReview2 = new Review("Not bad", 3);
//        List<Review> mockReviews = Arrays.asList(mockReview1, mockReview2);
//
//        // Mock the service response
//        Mockito.when(reviewService.getReviewByUser(1)).thenReturn(mockReviews);
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/user/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())  // Status code should be 404 as per the method logic
//                .andExpect(jsonPath("$.code").value(404))  // Check code
//                .andExpect(jsonPath("$.msg").value("All reviews read successfully"))  // Check message
//                .andExpect(jsonPath("$.data[0].reviewText").value("Great product"))  // Check first review's text
//                .andExpect(jsonPath("$.data[0].rating").value(5))  // Check first review's rating
//                .andExpect(jsonPath("$.data[1].reviewText").value("Not bad"))  // Check second review's text
//                .andExpect(jsonPath("$.data[1].rating").value(3))  // Check second review's rating
//                .andDo(print());
//    }
//
//    @Test
//    public void searchReviewByUsername() throws Exception {
//
//        // Create mock reviews for a user
//        Review mockReview1 = new Review("Great product", 5);
//        Review mockReview2 = new Review("Not bad", 3);
//        List<Review> mockReviews = Arrays.asList(mockReview1, mockReview2);
//
//        // Mock the service response
//        Mockito.when(reviewService.getReviewByUserName("client1")).thenReturn(mockReviews);
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/username/user1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())  // Status code should be 200 as per the method logic
//                .andExpect(jsonPath("$.code").value(200))  // Check code
//                .andExpect(jsonPath("$.msg").value("All reviews read successfully"))  // Check message
//                .andExpect(jsonPath("$.data[0].reviewText").value("Great product"))  // Check first review's text
//                .andExpect(jsonPath("$.data[0].rating").value(5))  // Check first review's rating
//                .andExpect(jsonPath("$.data[1].reviewText").value("Not bad"))  // Check second review's text
//                .andExpect(jsonPath("$.data[1].rating").value(3))  // Check second review's rating
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void getAllSortedReviews_ValidFieldAndOrder() throws Exception {
//        // Create mock reviews
//        Review mockReview1 = new Review("Great product", 5);
//        Review mockReview2 = new Review("Not bad", 3);
//        List<Review> mockReviews = Arrays.asList(mockReview1, mockReview2);
//
//        // Mock the service response
//        Mockito.when(reviewService.getALLReviewWithSort("rating", "asc")).thenReturn(mockReviews);
//
//        // Perform GET request
//        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/getAllSortedReviews/rating/asc")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.data[0].reviewText").value("Great product"))
//                .andExpect(jsonPath("$.data[1].reviewText").value("Not bad"))
//                .andDo(print());
//    }
//
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
//    public void getAllReviewsWithPaginationAndSort_ValidRequest() throws Exception {
//        // Create mock reviews and page
//        Review mockReview1 = new Review("Great product", 5);
//        Review mockReview2 = new Review("Not bad", 3);
//        Page<Review> mockPage = new PageImpl<>(Arrays.asList(mockReview1, mockReview2), PageRequest.of(0, 10), 2);
//
//        // Mock the service response
//        Mockito.when(reviewService.getAllReviewsWithPaginationAndSort(0, 10, "asc", "rating")).thenReturn(mockPage);
//
//        // Create PaginationRequest DTO
//        PaginationRequest request = new PaginationRequest();
//        request.setOffset(0);
//        request.setPageSize(10);
//        request.setOrder("asc");
//        request.setField("rating");
//
//        // Perform POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/reviews/getAllReviewsWithPaginationAndSort")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.data.content[0].reviewText").value("Great product"))
//                .andExpect(jsonPath("$.data.content[1].reviewText").value("Not bad"))
//                .andDo(print());
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
