package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResumeDaoI extends JpaRepository<Resume, Long> {
    @Query(value = "SELECT distinct e FROM Resume e where e.date between :startDate and :endDate")
    List<Resume> getResumesByDatePeriod(LocalDateTime startDate, LocalDateTime endDate);
}
