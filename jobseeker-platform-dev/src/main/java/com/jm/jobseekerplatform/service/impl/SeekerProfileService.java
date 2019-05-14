package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.SeekerProfileDAO;
import com.jm.jobseekerplatform.model.SeekerProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("seekerProfileService")
@Transactional
public class SeekerProfileService extends AbstractService<SeekerProfile> {

    @Autowired
    private SeekerProfileDAO dao;
}
