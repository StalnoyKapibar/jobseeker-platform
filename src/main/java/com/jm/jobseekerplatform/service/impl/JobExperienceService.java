package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.JobExperienceDAO;
import com.jm.jobseekerplatform.model.JobExperience;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobExperienceService extends AbstractService<JobExperience> {

    @Autowired
    JobExperienceDAO dao;
}
