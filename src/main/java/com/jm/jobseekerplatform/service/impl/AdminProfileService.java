package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.AdminProfileDAO;
import com.jm.jobseekerplatform.model.profiles.ProfileAdmin;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("adminProfileService")
@Transactional
public class AdminProfileService extends AbstractService<ProfileAdmin> {

    @Autowired
    private AdminProfileDAO dao;
}
