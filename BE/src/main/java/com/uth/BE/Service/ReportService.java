package com.uth.BE.Service;

import com.uth.BE.Entity.Report;
import com.uth.BE.Entity.Review;
import com.uth.BE.Entity.User;
import com.uth.BE.Entity.model.StatusReport;
import com.uth.BE.Repository.UserRepository;
import com.uth.BE.Service.Interface.IReportService;
import com.uth.BE.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements IReportService{

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    public ReportService(ReportRepository reportRepository,UserRepository userRepository){
        super();
        this.reportRepository =reportRepository;
        this.userRepository=userRepository;
    }

    @Override
    public List<Report> findAll(){return reportRepository.findAll();}

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public void delete(int id) {
        reportRepository.deleteById(id);
    }

    @Override
    public Optional<Report> findById(int id) {
        Optional<Report> report = Optional.ofNullable(reportRepository.findById(id).orElse(null));
        return report;
    }

    @Override
    public Report update(Report report) {
        Report exiting = reportRepository.findById(report.getReport_id()).orElse(null);
        exiting.setReportReason(report.getReportReason());
        exiting.setReportTitle(report.getReportTitle());
        exiting.setReportImg(report.getReportImg());
        exiting.setReport_date(new Timestamp(report.getReport_date().getTime()));
        return reportRepository.save(exiting);

    }


    @Override
    public List<Report> findReportByUser(int userID) {
        User u = userRepository.findById(userID).orElse(null);
        if (u != null) {
            return reportRepository.findByReportedBy(u);
        } else {
            return List.of();
        }
    }

    @Override
    public List<Report> getReportByUserName(String username) {
        User u = userRepository.findByUsername(username).orElse(null);
        if (u != null) {
            return reportRepository.findByReportedBy(u);
        } else {
            return List.of();
        }
    }

    @Override
    public void changeStatusReport(Report report, String status) {
        try{
            report.setStatus(StatusReport.valueOf(status));
            update(report);
        } catch (Exception e) {
            System.err.println("Error occurred while changing status report: " + e.getMessage());
        }

    }

    @Override
    public List<Report> getALLReportWithSort(String field, String order) {
        // asc -> tăng, desc -> down
        Sort.Direction sortDirection = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        // check filed is correctly
        Field reportField = ReflectionUtils.findField(Report.class, field);

        if (reportField == null) {
            throw new IllegalArgumentException("Field '" + field + "' does not exist in Report class");
//             return reportRepository.findAll();
        }
        // Tạo đối tượng Sort với filed  và order
        Sort sort = Sort.by(sortDirection, field);

        return reportRepository.findAll(sort);
    }


    @Override
    public Page<Report> getAllReportsWithPaginationAndSort(int pageNumber, int pageSize, String direction, String properties) {
        Sort.Direction directed = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, directed, properties);
        return reportRepository.findAll(pageable);
    }





}
