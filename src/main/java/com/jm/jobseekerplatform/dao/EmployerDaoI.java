package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Employer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerDaoI extends JpaRepository<Employer,Long> {
    Page<Employer> findAll(Pageable pageable);
}
