package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.EmployerProfileDAO;
import com.jm.jobseekerplatform.model.EmployerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("employerProfileService")
@Transactional
public class EmployerProfileService extends AbstractService<EmployerProfile> {

    @Autowired
    private EmployerProfileDAO dao;

}
