package com.uth.BE.Entity;

import java.sql.Timestamp;

import com.uth.BE.Entity.model.StatusReport;
import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="report_id")
    private int report_id ;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id",nullable = false)
    private Product  product;

    @Column(name="report_title",columnDefinition = "TEXT", nullable = false)
    private String report_title;

    @Column(name = "report_reason", columnDefinition = "TEXT", nullable = false)
    private String report_reason;

    @Column(name = "report_img", columnDefinition = "TEXT", nullable = false)
    private String report_img;

    //    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "report_date", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Timestamp report_date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('pending', 'resolved', 'dismissed') DEFAULT 'pending'")
    private StatusReport status = StatusReport.PENDING;

    public Report() {
        super();
    }

    public Report(String report_reason) {
        super();
        this.report_reason = report_reason;
    }

    public Report(String reportReason, String reportTitle, String reportImg) {
        super();
        this.report_reason=reportReason;
        this.report_title=reportTitle;
        this.report_img=reportImg;
    }


    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getReportTitle() {
        return report_title;
    }

    public void setReportTitle(String report_title) {
        this.report_title = report_title;
    }

    public String getReportReason() {
        return report_reason;
    }

    public void setReportReason(String report_reason) {
        this.report_reason = report_reason;
    }

    public String getReportImg() {
        return report_img;
    }

    public void setReportImg(String report_img) {
        this.report_img = report_img;
    }

    public Timestamp getReport_date() {
        return report_date;
    }

    public void setReport_date(Timestamp report_date) {
        this.report_date = report_date;
    }

    public StatusReport getStatus() {
        return status;
    }

    public void setStatus(StatusReport status) {
        this.status = status;
    }
}

