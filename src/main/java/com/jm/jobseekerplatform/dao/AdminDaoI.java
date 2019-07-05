package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDaoI extends JpaRepository<Admin,Long> {
    Page<Admin> findAll(Pageable pageable);
}
