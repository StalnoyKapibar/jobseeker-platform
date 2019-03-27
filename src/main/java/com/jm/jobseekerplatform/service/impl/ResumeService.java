package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.ResumeDAO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("resumeService")
@Transactional
public class ResumeService extends AbstractService<Resume> {

    @Autowired
    @Qualifier("resumeDAO")
    private ResumeDAO dao;
}
