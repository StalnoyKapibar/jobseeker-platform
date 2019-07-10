package com.jm.jobseekerplatform.dao;

import com.jm.jobseekerplatform.model.users.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDaoI extends JpaRepository<AdminUser,Long> {
    Page<AdminUser> findAll(Pageable pageable);
}
