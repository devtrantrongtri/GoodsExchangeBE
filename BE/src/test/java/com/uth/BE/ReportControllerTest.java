//package com.uth.BE;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.uth.BE.Controller.ReportController;
//import com.uth.BE.Entity.Product;
//import com.uth.BE.Entity.Report;
//import com.uth.BE.Entity.User;
//import com.uth.BE.Service.Interface.IProductService;
//import com.uth.BE.Service.Interface.IReportService;
//import com.uth.BE.Service.Interface.IUserService;
//import com.uth.BE.Service.JwtService;
//import com.uth.BE.Service.MyUserDetailService;
//import com.uth.BE.dto.req.PaginationRequest;
//import com.uth.BE.dto.req.ReportReq;
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
//@WebMvcTest(controllers = ReportController.class)
//@Import({JwtService.class, MyUserDetailService.class})
//public class ReportControllerTest {
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
//    private IReportService reportService;
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
//    public void testCreateReport() throws Exception {
//
//        Product mockProduct = new Product();
//        mockProduct.setProduct_id(1);
//        User mockUser = new User();
//        mockUser.setUserId(1);
//
//        ReportReq reportReq = new ReportReq();
//        reportReq.setProductId(1);
//        reportReq.setReportBy(1);
//        reportReq.setReportTitle("Report text");
//        reportReq.setReportReason("Report reason");
//        reportReq.setReportImg("img");
//
//        when(productService.getProductById(1)).thenReturn(Optional.of(mockProduct));
//        when(userService.getUserById(1)).thenReturn(Optional.of(mockUser));
//        when(reportService.save(Mockito.any(Report.class))).thenReturn(new Report());
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/reports")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(reportReq)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.code").value(201))
//                .andExpect(jsonPath("$.msg").value("Report created successfully"))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void findAllReports_UserAuthenticated_Success() throws Exception {
//        // Mock report list
//        Report mockReport1 = new Report();
//        Report mockReport2 = new Report();
//        List<Report> mockReports = Arrays.asList(mockReport1, mockReport2);
//
//        // Mock the service response
//        Mockito.when(reportService.findAll()).thenReturn(mockReports);
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reports")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Successfully get all reports"))
//                .andExpect(jsonPath("$.data[0].reportText").value("Great product"))
//                .andExpect(jsonPath("$.data[0].severity").value(5))
//                .andExpect(jsonPath("$.data[1].reportText").value("Not bad"))
//                .andExpect(jsonPath("$.data[1].severity").value(3))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    public void findReportById_UserAuthenticated_ReportFound() throws Exception {
//        // Mock report
//        Report mockReport = new Report();
//        mockReport.setReport_id(1);  // Set ID to match the path variable
//
//        // Mock service response
//        Mockito.when(reportService.findById(1)).thenReturn(Optional.of(mockReport));
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reports/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Successfully get this report"))
//                .andExpect(jsonPath("$.data.reportText").value("Great product"))
//                .andExpect(jsonPath("$.data.severity").value(5))
//                .andDo(print());
//    }
//
//    @Test
//    public void getReportsByUser() throws Exception {
//        // Create mock reports for a user
//        Report mockReport1 = new Report();
//        Report mockReport2 = new Report();
//        List<Report> mockReports = Arrays.asList(mockReport1, mockReport2);
//
//        // Mock the service response
//        Mockito.when(reportService.findReportByUser(1)).thenReturn(mockReports);
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reports/user/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())  // Status code should be 404 as per the method logic
//                .andExpect(jsonPath("$.code").value(404))
//                .andExpect(jsonPath("$.msg").value("All reports read successfully"))
//                .andExpect(jsonPath("$.data[0].reportText").value("Great product"))
//                .andExpect(jsonPath("$.data[0].severity").value(5))
//                .andExpect(jsonPath("$.data[1].reportText").value("Not bad"))
//                .andExpect(jsonPath("$.data[1].severity").value(3))
//                .andDo(print());
//    }
//
//    @Test
//    public void searchReportsByUsername() throws Exception {
//
//        // Create mock reports for a user
//        Report mockReport1 = new Report();
//        Report mockReport2 = new Report();
//        List<Report> mockReports = Arrays.asList(mockReport1, mockReport2);
//
//        // Mock the service response
//        Mockito.when(reportService.getReportByUserName("client1")).thenReturn(mockReports);
//
//        // Perform GET request with an authenticated user
//        mockMvc.perform(MockMvcRequestBuilders.get("/reports/username/user1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())  // Status code should be 200 as per the method logic
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("All reports read successfully"))
//                .andExpect(jsonPath("$.data[0].reportText").value("Great product"))
//                .andExpect(jsonPath("$.data[0].severity").value(5))
//                .andExpect(jsonPath("$.data[1].reportText").value("Not bad"))
//                .andExpect(jsonPath("$.data[1].severity").value(3))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void getAllSortedReports_ValidFieldAndOrder() throws Exception {
//        // Create mock reports
//        Report mockReport1 = new Report();
//        Report mockReport2 = new Report();
//        List<Report> mockReports = Arrays.asList(mockReport1, mockReport2);
//
//        // Mock the service response
//        Mockito.when(reportService.getALLReportWithSort("severity", "asc")).thenReturn(mockReports);
//
//        // Perform GET request
//        mockMvc.perform(MockMvcRequestBuilders.get("/reports/getAllSortedReports/severity/asc")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.data[0].reportText").value("Not bad"))
//                .andExpect(jsonPath("$.data[1].reportText").value("Great product"))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN", "MODERATOR", "CLIENT"})
//    public void getAllReportsWithPaginationAndSort_ValidRequest() throws Exception {
//        // Create mock reports and page
//        Report mockReport1 = new Report();
//        Report mockReport2 = new Report();
//        Page<Report> mockPage = new PageImpl<>(Arrays.asList(mockReport1, mockReport2), PageRequest.of(0, 10), 2);
//
//        // Mock the service response
//        Mockito.when(reportService.getAllReportsWithPaginationAndSort(0, 10, "asc", "severity")).thenReturn(mockPage);
//
//        // Create PaginationRequest DTO
//        PaginationRequest request = new PaginationRequest();
//        request.setOffset(0);
//        request.setPageSize(10);
//        request.setOrder("asc");
//        request.setField("severity");
//
//        // Perform POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/reports/getAllReportsWithPaginationAndSort")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("success"))
//                .andExpect(jsonPath("$.data.content[0].reportText").value("Not bad"))
//                .andExpect(jsonPath("$.data.content[1].reportText").value("Great product"))
//                .andDo(print());
//    }
//}
