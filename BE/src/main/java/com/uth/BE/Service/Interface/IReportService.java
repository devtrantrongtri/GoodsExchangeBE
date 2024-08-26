package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Report;
import com.uth.BE.Entity.Review;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IReportService {
    public List<Report> findAll();
    public Report save(Report report);
    public void delete(int id);
    public Optional<Report> findById(int id);
    public Report update(Report report);

    List<Report> findReportByUser(int userID);

    void changeStatusReport(Report report, String status);

    List<Report> getReportByUserName(String username);

    List<Report> getALLReportWithSort(String field, String order);

    Page<Report> getAllReportsWithPaginationAndSort(@Min(value = 0, message = "Offset must be greater than or equal to 0") int offset, @Min(value = 1, message = "Page size must be greater than 0") int pageSize, @NotBlank(message = "Order cannot be blank") @Pattern(regexp = "^(asc|desc)$", message = "Order must be 'asc' or 'desc'") String order, @NotBlank(message = "Field cannot be blank") @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Field can only contain alphanumeric characters and underscores") String field);
}

