package com.uth.BE.Pojo;

import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="report_id")
    private int report_id ;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
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

    public Report() {
        super();
    }

    public Report(String report_reason) {
        super();
        this.report_reason = report_reason;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getReport_reason() {
        return report_reason;
    }

    public void setReport_reason(String report_reason) {
        this.report_reason = report_reason;
    }

    public String getReport_img() {
        return report_img;
    }

    public void setReport_img(String report_img) {
        this.report_img = report_img;
    }

    public Timestamp getReport_date() {
        return report_date;
    }

    public void setReport_date(Timestamp report_date) {
        this.report_date = report_date;
    }
}

