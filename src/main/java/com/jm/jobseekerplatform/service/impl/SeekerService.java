package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.SeekerDAO;
import com.jm.jobseekerplatform.model.Seeker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("seekerService")
@Transactional
public class SeekerService extends UserService {

    @Autowired
    private SeekerDAO dao;
}
