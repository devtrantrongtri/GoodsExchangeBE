package com.uth.BE.Controller;


import com.uth.BE.Entity.*;
import com.uth.BE.Repository.ReportRepository;
import com.uth.BE.Service.Interface.IProductService;
import com.uth.BE.Service.Interface.IReportService;
import com.uth.BE.Service.Interface.IUserService;
import com.uth.BE.Service.ReportService;
import com.uth.BE.dto.req.ReportReq;
import com.uth.BE.dto.res.GlobalRes;
import org.springframework.beans.factory.annotation.Autowired;
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
        Report rp = new Report(reportReq.getReportReason(), reportReq.getReportTitle(), reportReq.getReportImg());
        Optional<Product> product = productService.getProductById(reportReq.getProductId());
        Optional<User> user = userService.getUserById(reportReq.getReportBy());
        rp.setProduct(product.orElse(null));
        rp.setReportedBy(user.orElse(null));

        Report savedReport = reportService.save(rp);

        if (savedReport != null) {
            return new GlobalRes<>(HttpStatus.CREATED, "Successfully create this report", rp);
        }

        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed create this report", null);
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
    public GlobalRes<Report> updateReport(@PathVariable int id, @RequestBody Report report) {
        Optional<Report> reportOptional = reportService.findById(id);
        if (reportOptional.isPresent()) {
            Report rp = reportOptional.get();
            rp.setReportTitle(report.getReportTitle());
            rp.setReportReason(report.getReportReason());
            rp.setReportImg(report.getReportImg());
            Report update = reportService.save(rp);

            return new GlobalRes<>(HttpStatus.OK, "Successfully updated the report", update);
        }
        return new GlobalRes<>(HttpStatus.BAD_REQUEST, "Failed to update the report", null);
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


}
