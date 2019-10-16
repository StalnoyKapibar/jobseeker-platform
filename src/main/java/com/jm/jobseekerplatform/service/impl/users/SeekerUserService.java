package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.SeekerUserDaoI;
import com.jm.jobseekerplatform.dao.impl.profiles.SeekerProfileDAO;
import com.jm.jobseekerplatform.dao.impl.users.SeekerUserDAO;
import com.jm.jobseekerplatform.model.profiles.SeekerProfile;
import com.jm.jobseekerplatform.model.users.SeekerUser;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("seekerUserService")
@Transactional
public class SeekerUserService extends AbstractService<SeekerUser> {

    @Autowired
    private SeekerUserDaoI seekerUserDaoI;

    @Autowired
    private SeekerUserDAO seekerUserDAO;

    @Autowired
    private SeekerProfileDAO seekerProfileDAO;

    public Page<SeekerUser> findAll(Pageable pageable) {
        return seekerUserDaoI.findAll(pageable);
    }

    public SeekerUser getByProfileId(Long seekerProfileId) {
        return seekerUserDAO.getByProfileId(seekerProfileId);
    }

    public SeekerProfile getSeekerProfileByResumeID(long id) {
        return seekerProfileDAO.getSeekerProfileByResumeID(id);
    }

    public List<SeekerUser> getSeekerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate){
        return seekerUserDaoI.getSeekerUsersByDatePeriod(startDate,endDate);
    }
}
