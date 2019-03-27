package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.ResumeDAO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

public class ResumeService extends AbstractService<Resume> {

    @Autowired
    private ResumeDAO dao;
}
