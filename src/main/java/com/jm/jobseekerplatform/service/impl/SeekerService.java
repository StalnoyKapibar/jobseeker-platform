package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.SeekerDaoI;
import com.jm.jobseekerplatform.model.Seeker;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("seekerService")
@Transactional
public class SeekerService extends AbstractService<Seeker> {

    @Autowired
    private SeekerDaoI seekerDaoI;

    public Page<Seeker> findAll(Pageable pageable) {
        return seekerDaoI.findAll(pageable);
    }
}
