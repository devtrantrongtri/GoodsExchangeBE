package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.Report;

import java.util.List;
import java.util.Optional;

public interface IReportService {
    public List<Report> findAll();
    public void save(Report report);
    public void delete(int reportId);
    public Optional<Report> findById(int reportId);
    public void update(Report report);
}

