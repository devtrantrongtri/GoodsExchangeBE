package com.uth.BE.Repository;


import com.uth.BE.Entity.Report;
import com.uth.BE.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,Integer> {

    List<Report> findByReportedBy(User u);

}
