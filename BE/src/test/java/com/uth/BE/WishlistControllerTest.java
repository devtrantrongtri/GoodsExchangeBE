//package com.uth.BE;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.uth.BE.Controller.WishlistController;
//import com.uth.BE.Entity.WishList;
//import com.uth.BE.Service.Interface.IProductService;
//import com.uth.BE.Service.Interface.IUserService;
//import com.uth.BE.Service.Interface.IWishlistService;
//import com.uth.BE.Service.JwtService;
//import com.uth.BE.Service.MyUserDetailService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(WishlistController.class)
//@Import({JwtService.class, MyUserDetailService.class}) // Import your security configuration classes if needed
//public class WishlistControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private IWishlistService wishlistService;
//
//    @MockBean
//    private IUserService userService;
//
//    @MockBean
//    private IProductService productService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    @WithMockUser(username = "user1", roles = {"USER"})
////    public void testCreateWishList() throws Exception {
////        // Prepare mock data
////        User mockUser = new User();
////        mockUser.setUserId(1);
////
////        Product mockProduct = new Product();
////        mockProduct.setProduct_id(1);
////
////        when(userService.findByUsername("user1")).thenReturn(Optional.of(mockUser));
////        when(productService.getProductById(1)).thenReturn(Optional.of(mockProduct));
////        when(wishlistService.createWishList(Mockito.any(WishList.class))).thenReturn(null); // Adjust based on actual method
////
////        Map<String, Object> payload = new HashMap<>();
////        payload.put("product_id", 1);
////
////        mockMvc.perform(post("/wishlist/addWishlist")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(payload)))
////                .andExpect(status().isCreated())
////                .andExpect(jsonPath("$.code").value(201))
////                .andExpect(jsonPath("$.msg").value("Wish list item created successfully"))
////                .andDo(print());
////    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void testFindWishlistById() throws Exception {
//        WishList mockWishList = new WishList();
//        mockWishList.setId(1);
//
//        when(wishlistService.findById(1)).thenReturn(Optional.of(mockWishList));
//
//        mockMvc.perform(get("/wishlist/findWishlistById/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void testDeleteWishList() throws Exception {
//        mockMvc.perform(delete("/wishlist/deleteWishlist/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Wish list item deleted successfully"))
//                .andDo(print());
//    }
//
////    @Test
////    @WithMockUser(username = "user1", roles = {"USER"})
////    public void testGetAllWishlists() throws Exception {
////        WishList mockWishList1 = new WishList();
////        WishList mockWishList2 = new WishList();
////        List<WishList> mockWishLists = Arrays.asList(mockWishList1, mockWishList2);
////
////        when(wishlistService.getAllWishlists()).thenReturn(mockWishLists);
////
////        mockMvc.perform(get("/wishlist/getAllWishlists")
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.length()").value(2))
////                .andDo(print());
////    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void testUpdateWishlist() throws Exception {
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("product_id", 2);
//
//        mockMvc.perform(put("/wishlist/updateWishlist/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(payload)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Wishlist updated successfully"))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void testAddProductToWishList() throws Exception {
//        Map<String, Integer> payload = new HashMap<>();
//        payload.put("user_id", 1);
//        payload.put("product_id", 2);
//
//        mockMvc.perform(post("/wishlist/addProductToWishList")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(payload)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Product added to wishlist successfully"))
//                .andDo(print());
//    }
//
////    @Test
////    @WithMockUser(username = "user1", roles = {"USER"})
////    public void testGetProductsInWishlist() throws Exception {
////        Product mockProduct1 = new Product();
////        Product mockProduct2 = new Product();
////        List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct2);
////
////        when(wishlistService.getProductsInWishlist(1)).thenReturn(mockProducts);
////
////        mockMvc.perform(get("/wishlist/1/products")
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.length()").value(2))
////                .andDo(print());
////    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void testRemoveProductFromWishlist() throws Exception {
//        mockMvc.perform(delete("/wishlist/1/products/2")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Product removed successfully"))
//                .andDo(print());
//    }
//}
//
////    @Test
////    @WithMockUser(username = "user1", roles = {"USER"})
////    public void testGetWishlistAnalytics() throws Exception {
////        when(wishlistService.getWishlistAnalytics(1)).thenReturn("Analytics Data");
////
////        mockMvc.perform(get("/wishlist/1/analytics")
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk())
////                .andExpect(content().string("Analytics Data"))
////                .andDo(print());
////    }
//
////    @Test
////    @WithMockUser(username = "user1", roles = {"USER"})
////    public void testSortWishlistProducts() throws Exception {
////        Product mockProduct1 = new Product();
////        Product mockProduct2 = new Product();
////        List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct2);
////
////        when(wishlistService.sortWishlistProducts(1)).thenReturn(mockProducts);
////
////        mockMvc.perform(post("/wishlist/1/sort")
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.length()").value(2))
////                .andDo(print());
////    }
////}
