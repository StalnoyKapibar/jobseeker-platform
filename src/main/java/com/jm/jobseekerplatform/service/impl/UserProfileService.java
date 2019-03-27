package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.UserProfileDAO;
import com.jm.jobseekerplatform.model.UserProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userProfileService")
@Transactional
public class UserProfileService extends AbstractService<UserProfile> {

    @Autowired
    @Qualifier("userProfileDAO")
    private UserProfileDAO dao;
}
