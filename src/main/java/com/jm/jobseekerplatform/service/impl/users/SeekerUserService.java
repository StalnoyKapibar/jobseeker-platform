package com.jm.jobseekerplatform.service.impl.users;

import com.jm.jobseekerplatform.dao.interfaces.profiles.SeekerProfileDao;
import com.jm.jobseekerplatform.dao.interfaces.users.SeekerUserDao;
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
    private SeekerUserDao seekerUserDao;

    @Autowired
    private SeekerProfileDao seekerProfileDao;

    public Page<SeekerUser> findAll(Pageable pageable) {
        return seekerUserDao.findAll(pageable);
    }

    public SeekerUser getByProfileId(Long seekerProfileId) {
        return seekerUserDao.findByProfileId(seekerProfileId);
    }

    public SeekerProfile getSeekerProfileByResumeID(long id) {
        return seekerProfileDao.findByResumeId(id);
    }

    public List<SeekerUser> getSeekerUsersByDatePeriod(LocalDateTime startDate, LocalDateTime endDate){
        return seekerUserDao.getSeekerUsersByDatePeriod(startDate,endDate);
    }

}
