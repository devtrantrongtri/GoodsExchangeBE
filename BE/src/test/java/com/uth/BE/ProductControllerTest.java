package com.uth.BE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.ProductController;
import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.Interface.ICategoryService;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.req.CategoryPaginationRequest;
import com.uth.BE.dto.req.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class ProductControllerTest {

    @MockBean
    private MyUserDetailService myUserDetailService;


    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @MockBean
    private IUserService userService;

    @MockBean
    private ICategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(
                new Product("Product1", "Description1", BigDecimal.valueOf(100), "Available")
        ));

        mockMvc.perform(get("/products/get_all_product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200)) // Kiểm tra giá trị mã trạng thái
                .andExpect(jsonPath("$.msg").value("success")) // Kiểm tra thông điệp
                .andExpect(jsonPath("$.data[0].title").value("Product1")) // Kiểm tra tiêu đề sản phẩm
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testFindProductById() throws Exception {
        Product product = new Product("Product1", "Description1", BigDecimal.valueOf(100), "Available");
        product.setProduct_id(1); // Make sure to set productId if it's a required field
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/find_product_by_id/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("Product1"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testCreateProduct() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("category_id", 1);
        payload.put("title", "Product1");
        payload.put("description", "Description1");
        payload.put("price", 100.0);
        payload.put("status", "Available");

        User user = new User();
        user.setUserId(1);
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(categoryService.findById(1)).thenReturn(new Category());
        doNothing().when(productService).createProduct(any(Product.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/products/create_product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.msg").value("Product created successfully"))
                .andDo(print());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void testUpdateProduct() throws Exception {
        // Tạo sản phẩm và gán sellerId cho sản phẩm
        Product existingProduct = new Product("Product1", "Description1", BigDecimal.valueOf(100), "Available");
        existingProduct.setProduct_id(1);
        User seller = new User();
        seller.setUserId(1); // Đảm bảo sellerId trùng với currentUserId
        existingProduct.setSeller(seller);

        // Thiết lập dữ liệu giả lập
        when(productService.getProductById(1)).thenReturn(Optional.of(existingProduct));
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(seller));
        when(categoryService.findById(anyInt())).thenReturn(new Category());
        doNothing().when(productService).updateProduct(any(Product.class));

        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "UpdatedProduct");
        payload.put("description", "UpdatedDescription");
        payload.put("price", 150.0);
        payload.put("status", "Unavailable");
        payload.put("category_id", 1);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/update_product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(csrf())) // Thêm CSRF token nếu bảo mật yêu cầu
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("Product updated successfully"))
                .andDo(print());
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
//    public void deleteProduct() throws Exception {
//        Product product = new Product();
//        when(productService.getProductById(anyInt())).thenReturn(Optional.of(product));
//
//        mockMvc.perform(delete("/products/delete_product/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.msg").value("Product deleted successfully"))
//                .andExpect(jsonPath("$.code").value(200))
//                .andDo(print());
//    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void searchProductsByUser() throws Exception {
        int userId = 1;

        // Giả lập User và Product tồn tại
        User user = new User();
        user.setUserId(userId);

        Product product1 = new Product();
        Product product2 = new Product();

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(productService.searchProductsByUser(Optional.of(user))).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/products/find_product_by_user_id/1", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Products found"))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void searchProductsByCategory() throws Exception {
        int categoryId = 1;

        // Giả lập Category và Product tồn tại
        Category category = new Category();
        category.setCategoryId(categoryId);

        Product product1 = new Product();
        Product product2 = new Product();

        when(categoryService.findById(categoryId)).thenReturn(category);
        when(productService.searchProductsByCategory(category)).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/products/find_product_by_category_id/1", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Products found"))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void searchProductsByTitle() throws Exception {
        String title = "Sample Title";

        // Giả lập có sản phẩm với title tương ứng
        Product product1 = new Product();
        Product product2 = new Product();

        when(productService.searchProductsByTitle(title)).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/products/find_product_by_title/Sample Title", title)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Products found"))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void searchProductsByPrice() throws Exception {
        BigDecimal minPrice = new BigDecimal("100.0");
        BigDecimal maxPrice = new BigDecimal("500.0");

        Product productInRange = new Product();
        productInRange.setPrice(new BigDecimal("200.0"));

        Product productOutOfRange = new Product();
        productOutOfRange.setPrice(new BigDecimal("600.0"));

        when(productService.searchProductsByPrice(minPrice, maxPrice)).thenReturn(List.of(productInRange));

        mockMvc.perform(get("/products/find_product_by_price/")
                        .param("minPrice", minPrice.toString())
                        .param("maxPrice", maxPrice.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Products found"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))  // Chỉ có 1 sản phẩm
                .andExpect(jsonPath("$.data[0].price").value(productInRange.getPrice()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void getAllProductsWithPaginationAndSort() throws Exception {
        Product product1 = new Product();
        product1.setProduct_id(1);
        product1.setTitle("product1");

        Product product2 = new Product();
        product1.setProduct_id(2);
        product1.setTitle("product2");

        List<Product> products = Arrays.asList(product1, product2);
        Page<Product> page = new PageImpl<>(products, PageRequest.of(0, 10, Sort.by("title")), products.size());

        CategoryPaginationRequest request = new CategoryPaginationRequest();
        request.setOffset(0);
        request.setPageSize(10);
        request.setOrder("asc");
        request.setField("title");

        when(productService.getAllProductsWithPaginationAndSort(
                request.getOffset(), request.getPageSize(), request.getOrder(), request.getField())).thenReturn(page);

        mockMvc.perform(get("/products/getAllProductsWithPaginationAndSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())  // Kiểm tra trạng thái HTTP là 200 OK
                .andExpect(jsonPath("$.msg").value("success"))  // Kiểm tra thông báo phản hồi
                .andExpect(jsonPath("$.code").value(200))  // Kiểm tra mã trạng thái
                .andExpect(jsonPath("$.data.content[0].title").value("product2"))  // Kiểm tra dữ liệu sản phẩm đầu tiên
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void getProductsByKeyword() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setProductId(1);
        product.setTitle("Smartphone");
        product.setDescription("Latest model smartphone with advanced features.");
        product.setStatus("AVAILABLE");
        product.setImageUrls(Arrays.asList("images/products/1.jpg"));

        List<ProductDTO> products = Arrays.asList(product);

        when(productService.getProductsWithImage("smart")).thenReturn(products);

        mockMvc.perform(get("/products/keyword/smart", "smart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Kiểm tra trạng thái HTTP là 200 OK
                .andExpect(jsonPath("$.msg").value("success"))  // Kiểm tra thông báo phản hồi
                .andExpect(jsonPath("$.code").value(200))  // Kiểm tra mã trạng thái
                .andExpect(jsonPath("$.data[0].productId").value(1))  // Kiểm tra ID sản phẩm
                .andExpect(jsonPath("$.data[0].title").value("Smartphone"))  // Kiểm tra tiêu đề sản phẩm
                .andExpect(jsonPath("$.data[0].description").value("Latest model smartphone with advanced features."))  // Kiểm tra mô tả sản phẩm
                .andExpect(jsonPath("$.data[0].status").value("AVAILABLE"))  // Kiểm tra trạng thái sản phẩm
                .andExpect(jsonPath("$.data[0].imageUrls[0]").value("images/products/1.jpg"))  // Kiểm tra URL hình ảnh
                .andDo(print());
    }
}
