package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Report;
import com.uth.BE.Entity.Review;

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

}

