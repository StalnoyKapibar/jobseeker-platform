package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.EmployerDaoI;
import com.jm.jobseekerplatform.model.Employer;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("employerService")
@Transactional
public class EmployerService extends AbstractService<Employer> {

    @Autowired
    private EmployerDaoI employerDaoI;

    public Page<Employer> findAll(Pageable pageable) {
        return employerDaoI.findAll(pageable);
    }
}
