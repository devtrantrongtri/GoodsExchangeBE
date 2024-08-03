package com.uth.BE.Controller;


import com.uth.BE.Pojo.Report;
import com.uth.BE.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/createReport")
    public Report createReport(@RequestBody Report report){ reportService.save(report);
        return report;
    }

    @GetMapping("/findReportById/{reportId}")
    public Optional<Report> findReportById(@PathVariable int reportId){return reportService.findById(reportId);}

    @GetMapping("/findAllReport")
    public List<Report> findAllReport(){return reportService.findAll();}

    @PostMapping("/updateReport")
    public Report updateReport(@RequestBody Report report){reportService.update(report);
        return report;
    }

    @PostMapping("/deleteReportById/{reportId}")
    public void deleteReport(@PathVariable int reportId) {reportService.delete(reportId);}
}
