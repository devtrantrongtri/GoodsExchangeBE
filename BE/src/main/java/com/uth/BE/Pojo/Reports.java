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
public class Reports {
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

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "report_date", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Timestamp report_date;

    public Reports() {
        super();
    }

    public Reports( String report_reason) {
        super();
        this.report_reason = report_reason;
    }
}

