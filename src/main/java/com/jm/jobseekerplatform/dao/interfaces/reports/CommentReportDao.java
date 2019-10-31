package com.jm.jobseekerplatform.dao.interfaces.reports;

import com.jm.jobseekerplatform.model.reports.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReportDao extends JpaRepository<CommentReport, Long> {

}
