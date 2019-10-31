package com.jm.jobseekerplatform.dao.interfaces.reports;

import com.jm.jobseekerplatform.model.reports.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDao extends JpaRepository<Report, Long> {
}
