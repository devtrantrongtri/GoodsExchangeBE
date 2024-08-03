package com.uth.BE.Service;

import com.uth.BE.Pojo.Report;
import com.uth.BE.Pojo.Review;
import com.uth.BE.Service.Interface.IReportService;
import com.uth.BE.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService implements IReportService{

    @Autowired
    private ReportRepository repo;

    public ReportService(ReportRepository repo){
        super();
        this.repo=repo;
    }

    @Override
    public List<Report> findAll(){return repo.findAll();}

    @Override
    public void save(Report report) {
        repo.findAll();
    }

    @Override
    public void delete(int reportId) {
        repo.deleteById(reportId);
    }

    @Override
    public Optional<Report> findById(int reportId) {
        Optional<Report> report = Optional.ofNullable(repo.findById(reportId).orElse(null));
        return report;
    }

    @Override
    public void update(Report report) {
        Report exiting = repo.findById(report.getReport_id()).orElse(null);
        exiting.setReport_reason(report.getReport_reason());
        exiting.setReport_title(report.getReport_title());
        exiting.setReport_img(report.getReport_img());
        exiting.setReport_date(new Timestamp(report.getReport_date().getDate()));
        repo.save(exiting);

    }
}
