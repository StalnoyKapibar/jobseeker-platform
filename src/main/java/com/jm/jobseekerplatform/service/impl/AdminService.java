package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.AdminDaoI;
import com.jm.jobseekerplatform.model.Admin;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminService")
@Transactional
public class AdminService extends AbstractService<Admin> {

    @Autowired
    private AdminDaoI adminDaoI;

    public Page<Admin> findAll(Pageable pageable) {
        return adminDaoI.findAll(pageable);
    }
}
