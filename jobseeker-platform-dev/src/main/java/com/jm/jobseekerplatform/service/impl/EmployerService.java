package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.EmployerDAO;
import com.jm.jobseekerplatform.model.Employer;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("employerService")
@Transactional
public class EmployerService extends UserService {

    @Autowired
    private EmployerDAO dao;
}
