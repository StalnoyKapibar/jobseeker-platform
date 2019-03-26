package com.jm.jobseekerplatform.service.impl;

import com.jm.jobseekerplatform.dao.impl.UserProfileDAO;
import com.jm.jobseekerplatform.model.UserProfile;
import com.jm.jobseekerplatform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserProfileService extends AbstractService<UserProfile> {

    @Autowired
    private UserProfileDAO dao;
}
