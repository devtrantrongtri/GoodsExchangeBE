package com.uth.BE;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uth.BE.Controller.CategoryController;
import com.uth.BE.Entity.Category;
import com.uth.BE.Entity.Product;
import com.uth.BE.Service.CategoryService;
import com.uth.BE.Service.JwtService;
import com.uth.BE.Service.MyUserDetailService;
import com.uth.BE.dto.req.CategoryPaginationRequest;
import org.junit.jupiter.api.Test;
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

import java.util.*;

@WebMvcTest(controllers = CategoryController.class)
@Import({JwtService.class, MyUserDetailService.class})
public class CategoryControllerTest {

    @MockBean
    private MyUserDetailService myUserDetailService;


    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void getAllCategories() throws Exception {
        Category category1 = new Category();
        Category category2 = new Category();

        when(categoryService.getAllCategories()).thenReturn(List.of(category1, category2));

        mockMvc.perform(get("/category/get_all_categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Categories retrieved successfully"))  // Kiểm tra thông báo phản hồi
                .andExpect(jsonPath("$.data").isArray())  // Kiểm tra dữ liệu trả về là một mảng
                .andExpect(jsonPath("$.data.length()").value(2))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void createCategory() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "smartPhone");

        mockMvc.perform(MockMvcRequestBuilders.post("/category/create_category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("Category created successfully"))
                .andExpect(jsonPath("$.code").value(201))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void updateCategory() throws Exception {
        Category existingCategory = new Category();
        existingCategory.setCategoryId(1);
        existingCategory.setName("Old Name");

        when(categoryService.getCategoryById(1)).thenReturn(Optional.of(existingCategory));
        doNothing().when(categoryService).updateCategory(any(Category.class));

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "UpdatedCategory");

        mockMvc.perform(MockMvcRequestBuilders.put("/category/update_category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("Category updated successfully"))
                .andDo(print());
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
//    public void deleteCategory() throws Exception {
//        Category category = new Category();
//        category.setCategoryId(1);
//        category.setName("Electronics");
//
//        when(categoryService.getCategoryById(1)).thenReturn(Optional.of(category));
//
//        mockMvc.perform(delete("/category/delete_category/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.msg").value("Category deleted successfully"))
//                .andExpect(jsonPath("$.code").value(200))
//                .andDo(print());
//    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
    public void getAllCategoriesWithPaginationAndSort() throws Exception {
        Category category1 = new Category();
        category1.setCategoryId(1);
        category1.setName("Electronics");

        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setName("Books");

        List<Category> categories = Arrays.asList(category1, category2);
        Page<Category> page = new PageImpl<>(categories, PageRequest.of(0, 10, Sort.by("name")), categories.size());

        CategoryPaginationRequest request = new CategoryPaginationRequest();
        request.setOffset(0);
        request.setPageSize(10);
        request.setOrder("asc");
        request.setField("name");

        when(categoryService.getAllCategoriesWithPaginationAndSort(
                request.getOffset(), request.getPageSize(), request.getOrder(), request.getField())).thenReturn(page);

        mockMvc.perform(get("/category/getAllCategoriesWithPaginationAndSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())  // Kiểm tra trạng thái HTTP là 200 OK
                .andExpect(jsonPath("$.msg").value("Categories retrieved successfully"))  // Kiểm tra thông báo phản hồi
                .andExpect(jsonPath("$.code").value(200))  // Kiểm tra mã trạng thái
                .andExpect(jsonPath("$.data.content[0].name").value("Electronics"))  // Kiểm tra dữ liệu danh mục
                .andExpect(jsonPath("$.data.content[1].name").value("Books"))
                .andDo(print());
    }
}
