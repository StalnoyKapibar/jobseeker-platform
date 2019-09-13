package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.SeekerShowNameDAO;
import com.jm.jobseekerplatform.model.SeekerShowName;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service("seekerTestService")
@Transactional
public class SeekerShowNameService extends AbstractService<SeekerShowName> {

    @Autowired
    private SeekerShowNameDAO dao;

    public List<SeekerShowName> getAllSeekerTestById(List<Long> id) {
        return dao.getAllSeekerTestById(id);
    }

}
