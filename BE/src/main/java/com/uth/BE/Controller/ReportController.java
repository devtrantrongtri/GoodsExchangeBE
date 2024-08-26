package com.uth.BE.Controller;


import com.uth.BE.Entity.*;
import com.uth.BE.Repository.ReportRepository;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.Interface.IReportService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.ReportService;
import com.uth.BE.dto.req.CommentReq;
import com.uth.BE.dto.req.PaginationRequest;
import com.uth.BE.dto.req.ReportReq;
import com.uth.BE.dto.res.GlobalRes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IReportService reportService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ReportRepository reportRepository;

    @PostMapping()
    public GlobalRes<Report> createReport(@RequestBody ReportReq reportReq) {
        // Tìm kiếm Product và User theo ID
        Optional<Product> productOpt = productService.getProductById(reportReq.getProductId());
        Optional<User> userOpt = userService.getUserById(reportReq.getReportBy());

        // Kiểm tra nếu Product không tồn tại
        if (!productOpt.isPresent()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", null);
        }

        // Kiểm tra nếu User không tồn tại
        if (!userOpt.isPresent()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", null);
        }

        // Tạo Report mới với Product và User đã tìm thấy
        Report rp = new Report(reportReq.getReportReason(), reportReq.getReportTitle(), reportReq.getReportImg());
        rp.setProduct(productOpt.get());
        rp.setReportedBy(userOpt.get());

        // Lưu Report vào cơ sở dữ liệu
        Report savedReport = reportService.save(rp);

        // Kiểm tra nếu Report đã được lưu thành công
        if (savedReport != null) {
            return new GlobalRes<>(HttpStatus.CREATED, "Successfully created the report", savedReport);
        }

        // Trả về phản hồi nếu việc lưu Report không thành công
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to create the report", null);
    }


    @GetMapping()
    public GlobalRes<List<Report>> readAllReport() {
        List<Report> rp = reportService.findAll();
        if (rp != null && !rp.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get all reports", rp);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get all reports", null);
    }

    @GetMapping("/{id}")
    public GlobalRes<Optional<Report>> readReportById(@PathVariable int id) {
        Optional<Report> reportOptional = reportService.findById(id);
        if (reportOptional.isPresent()) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully get this report", reportOptional);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed get this report,null", null);

    }


    @GetMapping("/user/{id}")
    public GlobalRes<List<Report>> getReportByUser(@PathVariable("id") int userID) {
        List<Report> rp = reportService.findReportByUser(userID);
        if (rp.isEmpty() || rp == null) {
            return new GlobalRes<>(HttpStatus.OK, "List empty", rp);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "All reports read successfully", rp);
    }

    @GetMapping("/username/{username}")
    public GlobalRes<List<Report>> searchReportByUsername(@PathVariable("username") String username) {
        List<Report> rp = reportService.getReportByUserName(username);
        if (rp.isEmpty() || rp == null) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "List empty", rp);
        }
        return new GlobalRes<>(HttpStatus.OK, "All reports read successfully", rp);
    }

//    @PutMapping("/updateReport")
//    public GlobalRes<Report> updatedReport(@RequestBody Report report) {
//        Report rp = reportService.update(report);
//        if (rp != null) {
//            return new GlobalRes<>(HttpStatus.OK, "Successfully update this report", null);
//        }
//        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update this report", null);
//    }


    @PutMapping("/updateReport/{id}")
    public GlobalRes<Report> updateReport(@PathVariable("id") int Id, @RequestBody ReportReq reportReq) {
        Optional<Report> existingReport = reportService.findById(Id);

        if (existingReport.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Comment not found", null);
        }

        Report updateReport = existingReport.get();
        updateReport.setReportTitle(reportReq.getReportTitle());
        updateReport.setReportReason(reportReq.getReportReason());
        updateReport.setReportImg(reportReq.getReportImg());

        Optional<User> user = userService.getUserById(reportReq.getReportBy());
        Optional<Product> product = productService.getProductById(reportReq.getProductId());

        if (user.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "User not found", null);
        }

        if (product.isEmpty()) {
            return new GlobalRes<>(HttpStatus.NOT_FOUND, "Product not found", null);
        }

        updateReport.setReportedBy(user.get());
        updateReport.setProduct(product.get());

        Report updatedReport = reportService.update(updateReport);

        if (updatedReport != null) {
            return new GlobalRes<>(HttpStatus.OK, "Successfully updated this report", updatedReport);
        }

        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update this report", null);
    }

    @DeleteMapping("/deleteReportById/{id}")
    public GlobalRes<Report> deleteReport(@PathVariable int id) {
        try{
            reportService.delete(id);
            return new GlobalRes<>(HttpStatus.OK, "Successfully delete this report", null);
        }catch (Exception e){
            e.printStackTrace();
            return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to delete this report3", null);
        }
    }

    @PutMapping("/{id}/status")
    public GlobalRes<Optional> changeStatus(@PathVariable int id, @RequestParam String status) {
        try {
            Optional<Report> rp = reportService.findById(id);
            if (rp.isPresent()) {
                Report report = rp.get();
                reportService.changeStatusReport(report, status);
                return new GlobalRes<>(HttpStatus.OK, "Report status updated successfully", null);
            } else {
                return new GlobalRes<>(HttpStatus.NOT_FOUND, "Report not found", null);
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update report status", null);
        }
    }

    @GetMapping("/getAllSortedReports/{field}/{order}")
    public GlobalRes<List<Report>> getAllSortedReport(@PathVariable String field,@PathVariable String order) {
        List<Report> reports = reportService.getALLReportWithSort(field,order);
        if (reports != null && !reports.isEmpty()) {
            return new GlobalRes<>(HttpStatus.OK,"success",reports);
        } else {
            return new GlobalRes<>(HttpStatus.NO_CONTENT,"success");
        }
    }
    //totalPages lấy dùng cho lastPage
//    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/getAllReportsWithPaginationAndSort")
    public GlobalRes<Page<Report>> getAllReportsWithPaginationAndSort(
            @Valid @RequestBody PaginationRequest request) {
        try {
            Page<Report> reportsPage = reportService.getAllReportsWithPaginationAndSort(
                    request.getOffset(), request.getPageSize(), request.getOrder(), request.getField());
            if (reportsPage.hasContent()) {
                return new GlobalRes<>(HttpStatus.OK.value(), "success", reportsPage);
            } else {
                return new GlobalRes<>(HttpStatus.NO_CONTENT.value(), "No reports found");
            }
        } catch (Exception e) {
            return new GlobalRes<>(HttpStatus.BAD_REQUEST.value(), "Invalid parameters: " + e.getMessage());
        }
    }




}
