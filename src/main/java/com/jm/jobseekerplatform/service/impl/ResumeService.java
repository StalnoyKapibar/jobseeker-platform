package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.ResumeDAO;
import com.jm.jobseekerplatform.model.Resume;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("resumeService")
@Transactional
public class ResumeService extends AbstractService<Resume> {

    @Autowired
    private ResumeDAO dao;

    public Page<Resume> getAllResumes(int limit, int page) {
        return dao.getAllResumes(limit, page);
    }
}
