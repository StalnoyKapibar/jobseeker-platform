package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.SeekerUserDaoI;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("seekerService")
@Transactional
public class SeekerUserService extends AbstractService<SeekerUser> {

    @Autowired
    private SeekerUserDaoI seekerUserDaoI;

    public Page<SeekerUser> findAll(Pageable pageable) {
        return seekerUserDaoI.findAll(pageable);
    }
}
