package com.uth.BE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.ProductImgController;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.ProductImg;
import com.uth.BE.Entity.model.FileExtension;
import com.uth.BE.Service.Interface.IProductImgService;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ProductImgController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class ProductImgControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductImgService productImgService;

    @MockBean
    private IProductService productService;

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
    public void testGetAllProductImgs_Success() throws Exception {
        List<ProductImg> productImgList = Arrays.asList(
                new ProductImg("Image1", "https://example.com/image1.jpg", FileExtension.JPG),
                new ProductImg("Image2", "https://example.com/image2.jpg", FileExtension.PNG)
        );

        when(productImgService.findAll()).thenReturn(productImgList);

        mockMvc.perform(get("/productimgs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully retrieved all product images"))
                .andExpect(jsonPath("$.data.length()").value(productImgList.size()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testCreateProductImg_Success() throws Exception {
        ProductImg productImg = new ProductImg("Test Image", "https://example.com/test.jpg", FileExtension.JPG);
        Product product = new Product(); // Dummy product

        when(productService.getProductById(1)).thenReturn(Optional.of(product));
        when(productImgService.save(any(ProductImg.class))).thenReturn(productImg);

        String payload = objectMapper.writeValueAsString(Map.of(
                "title", "Test Image",
                "imgUrl", "https://example.com/test.jpg",
                "fileExtension", "JPG",
                "productId", 1
        ));

        mockMvc.perform(post("/productimgs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("Successfully created the product image"))
                .andDo(print());
    }



    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetProductImgById_Success() throws Exception {
        ProductImg productImg = new ProductImg("Test Image", "https://example.com/test.jpg", FileExtension.JPG);

        when(productImgService.findById(1)).thenReturn(Optional.of(productImg));

        mockMvc.perform(get("/productimgs/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully retrieved the product image"))
                .andExpect(jsonPath("$.data.title").value("Test Image"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testUpdateProductImg_Success() throws Exception {
        ProductImg productImg = new ProductImg("Old Image", "https://example.com/old.jpg", FileExtension.JPG);
        ProductImg updatedImg = new ProductImg("Updated Image", "https://example.com/updated.jpg", FileExtension.PNG);

        when(productImgService.findById(1)).thenReturn(Optional.of(productImg));
        when(productImgService.save(any(ProductImg.class))).thenReturn(updatedImg);

        String payload = objectMapper.writeValueAsString(Map.of(
                "title", "Updated Image",
                "imgUrl", "https://example.com/updated.jpg",
                "fileExtension", "PNG"
        ));

        mockMvc.perform(put("/productimgs/1")
                        .with(csrf())  // Ensure CSRF token is present
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Successfully updated the product image"))
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testDeleteProductImg_Success() throws Exception {
        // Mock the service to ensure it doesn't throw an exception
        doNothing().when(productImgService).delete(1);

        mockMvc.perform(delete("/productimgs/deleteProductImg/1")
                        .with(csrf())  // Ensure CSRF token is included
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expect 200 OK
                .andExpect(jsonPath("$.msg").value("Successfully deleted the product image"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetProductImgsWithSort_Success() throws Exception {
        List<ProductImg> sortedProductImgs = Arrays.asList(
                new ProductImg("Image A", "https://example.com/imageA.jpg", FileExtension.JPG),
                new ProductImg("Image B", "https://example.com/imageB.jpg", FileExtension.PNG)
        );

        when(productImgService.findProductImgWithSort("title", "asc")).thenReturn(sortedProductImgs);

        mockMvc.perform(get("/productimgs/sort/title/asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Product images retrieved successfully"))
                .andExpect(jsonPath("$.data[0].title").value("Image A"))
                .andExpect(jsonPath("$.data[1].title").value("Image B"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetProductImgsWithPage_Success() throws Exception {
        List<ProductImg> productImgList = Arrays.asList(
                new ProductImg("Image1", "https://example.com/image1.jpg", FileExtension.JPG),
                new ProductImg("Image2", "https://example.com/image2.jpg", FileExtension.PNG)
        );

        Pageable pageable = PageRequest.of(0, 2);

        Page<ProductImg> productImgPage = new PageImpl<>(productImgList, pageable, productImgList.size());

        when(productImgService.findProductImgWithPage(0, 2)).thenReturn(productImgPage);

        mockMvc.perform(get("/productimgs/page/0/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Product images retrieved successfully"))
                .andExpect(jsonPath("$.data.content.length()").value(productImgList.size()))
                .andDo(print());
    }


}
